package com.dylibso.chicory.runtime;

import static com.dylibso.chicory.runtime.ConstantEvaluators.computeConstantValue;
import static java.lang.Math.min;

import com.dylibso.chicory.runtime.alloc.DefaultMemAllocStrategy;
import com.dylibso.chicory.runtime.alloc.MemAllocStrategy;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.UninstantiableException;
import com.dylibso.chicory.wasm.types.ActiveDataSegment;
import com.dylibso.chicory.wasm.types.DataSegment;
import com.dylibso.chicory.wasm.types.MemoryLimits;
import com.dylibso.chicory.wasm.types.PassiveDataSegment;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Represents the linear memory in the Wasm program. Can be shared
 * reference b/w the host and the guest.
 *
 * This is the preferred memory implementation on Android systems.
 */
public final class ByteBufferMemory implements Memory {

    private final MemoryLimits limits;
    private DataSegment[] dataSegments;
    private ByteBuffer buffer;
    private int nPages;

    private final MemAllocStrategy allocStrategy;

    public ByteBufferMemory(MemoryLimits limits) {
        this(limits, new DefaultMemAllocStrategy(Memory.bytes(limits.maximumPages())));
    }

    public ByteBufferMemory(MemoryLimits limits, MemAllocStrategy allocStrategy) {
        this.allocStrategy = allocStrategy;
        this.limits = limits;
        this.buffer =
                ByteBuffer.allocate(allocStrategy.initial(PAGE_SIZE * limits.initialPages()))
                        .order(ByteOrder.LITTLE_ENDIAN);
        this.nPages = limits.initialPages();
        if (limits.shared()) {
            monitors = new ConcurrentHashMap<>();
            notifyInProgress = new ConcurrentHashMap<>();
        } else {
            monitors = null;
            notifyInProgress = null;
        }
    }

    // atomic wait handling
    private final Map<Integer, AtomicInteger> monitors;
    private final Map<Integer, AtomicInteger> notifyInProgress;

    @Override
    public Object lock(int address) {
        if (!shared()) {
            // disable locking
            return new Object();
        }
        return monitors.computeIfAbsent(address, k -> new AtomicInteger(0));
    }

    private AtomicInteger nextMonitor(int address) {
        return monitors.compute(
                address,
                (k, v) -> {
                    if (v == null) {
                        return new AtomicInteger(1);
                    } else {
                        v.incrementAndGet();
                        return v;
                    }
                });
    }

    // this method should only be invoked guarded in a "synchronized (monitor)" section
    private int waitOnMonitor(int address, long timeout, AtomicInteger monitor) {
        long endTime = System.nanoTime() + timeout;
        try {
            while (!notifyInProgress.containsKey(address) // prevents spurious wakeup
                    && System.nanoTime() < endTime) {
                var waitTime = endTime - System.nanoTime();
                long millis = Math.max(waitTime / 1_000_000L, 0);
                int nanos = Math.max((int) (waitTime % 1_000_000L), 0);
                monitor.wait(millis, nanos);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            throw new ChicoryInterruptedException("Thread interrupted");
        }
        if (System.nanoTime() >= endTime) {
            return 2; // timeout
        } else {
            return 0; // wake
        }
    }

    private void endWaitOn(int address) {
        AtomicInteger notifyCount = notifyInProgress.get(address);
        if (notifyCount != null && notifyCount.decrementAndGet() == 0) {
            notifyInProgress.remove(address);
        }
        AtomicInteger monitor = monitors.get(address);
        if (monitor != null && monitor.decrementAndGet() == 0) {
            monitors.remove(address);
        }
    }

    // Wait until value at address != expected
    @Override
    public int waitOn(int address, int expected, long timeout) {
        if (!shared()) {
            throw new ChicoryException("Attempt to wait on a non-shared memory, not supported.");
        }
        AtomicInteger monitor = nextMonitor(address);

        synchronized (monitor) {
            try {
                if (buffer.getInt(address) == expected) {
                    return waitOnMonitor(
                            address, (timeout < 0) ? Long.MAX_VALUE : timeout, monitor);
                } else {
                    return 1; // not-equal
                }
            } finally {
                endWaitOn(address);
            }
        }
    }

    @Override
    public int waitOn(int address, long expected, long timeout) {
        if (!shared()) {
            throw new ChicoryException("Attempt to wait on a non-shared memory, not supported.");
        }
        AtomicInteger monitor = nextMonitor(address);

        synchronized (monitor) {
            try {
                if (buffer.getLong(address) == expected) {
                    return waitOnMonitor(
                            address, (timeout < 0) ? Long.MAX_VALUE : timeout, monitor);
                } else {
                    return 1; // not-equal
                }
            } finally {
                endWaitOn(address);
            }
        }
    }

    // Notify all waiters at this address
    @Override
    public int notify(int address, int maxThreads) {
        if (!shared()) {
            return 0;
        }

        AtomicInteger monitor = monitors.get(address);
        if (monitor == null) {
            return 0;
        }

        synchronized (monitor) {
            if (maxThreads < 0 || monitor.get() < maxThreads) {
                notifyInProgress.put(address, new AtomicInteger(monitor.get()));
                monitor.notifyAll();
            } else {
                var count = maxThreads;
                notifyInProgress.put(address, new AtomicInteger(monitor.get() - maxThreads));
                while (monitor.get() > 0 && count > 0) {
                    monitor.notify();
                    count--;
                }
            }
        }
        if (monitor.get() <= 0) {
            monitors.remove(address);
        }
        return monitor.get();
    }

    private ByteBuffer allocateByteBuffer(int capacity) {
        if (capacity > buffer.capacity()) {
            int nextCapacity = allocStrategy.next(buffer.capacity(), capacity);
            return ByteBuffer.allocate(nextCapacity).order(ByteOrder.LITTLE_ENDIAN);
        } else {
            return buffer;
        }
    }

    /**
     * Gets the size of the memory in number of pages
     */
    @Override
    public int pages() {
        return nPages;
    }

    @Override
    public int grow(int size) {
        var prevPages = nPages;
        var numPages = prevPages + size;

        if (numPages > maximumPages() || numPages < prevPages) {
            return -1;
        }

        var newBuffer = allocateByteBuffer(PAGE_SIZE * numPages);
        if (newBuffer != buffer) {
            var oldBuffer = buffer;
            var position = oldBuffer.position();
            oldBuffer.rewind();
            newBuffer.put(oldBuffer);
            newBuffer.position(position);
            buffer = newBuffer;
        }

        nPages = numPages;
        return prevPages;
    }

    @Override
    public int initialPages() {
        return this.limits.initialPages();
    }

    @Override
    public int maximumPages() {
        return min(this.limits.maximumPages(), RUNTIME_MAX_PAGES);
    }

    @Override
    public boolean shared() {
        return this.limits.shared();
    }

    @Override
    public void initialize(Instance instance, DataSegment[] dataSegments) {
        this.dataSegments = dataSegments;
        if (dataSegments == null) {
            return;
        }

        for (var s : dataSegments) {
            if (s instanceof ActiveDataSegment) {
                var segment = (ActiveDataSegment) s;
                var offsetExpr = segment.offsetInstructions();
                var data = segment.data();
                var offset = (int) computeConstantValue(instance, offsetExpr)[0];
                checkBounds(
                        offset,
                        data.length,
                        (PAGE_SIZE * nPages),
                        (msg) -> new UninstantiableException(msg));
                buffer.position(offset);
                buffer.put(data, 0, data.length);
            } else if (s instanceof PassiveDataSegment) {
                // Passive segment should be skipped
            } else {
                throw new ChicoryException("Data segment should be active or passive: " + s);
            }
        }
    }

    private static void checkBounds(
            int addr, int size, int limit, Function<String, ChicoryException> exceptionFactory) {
        if (addr < 0 || size < 0 || addr > limit || (size > 0 && ((addr + size) > limit))) {
            var errorMsg =
                    "out of bounds memory access: attempted to access address: "
                            + addr
                            + " but limit is: "
                            + limit
                            + " and size: "
                            + size;
            throw exceptionFactory.apply(errorMsg);
        }
    }

    private static RuntimeException outOfBoundsException(
            RuntimeException e, int addr, int size, int limit) {
        if (e instanceof IndexOutOfBoundsException
                || e instanceof BufferOverflowException
                || e instanceof BufferUnderflowException
                || e instanceof IllegalArgumentException
                || e instanceof NegativeArraySizeException) {
            var errorMsg =
                    "out of bounds memory access: attempted to access address: "
                            + addr
                            + " but limit is: "
                            + limit
                            + " and size: "
                            + size;
            return new WasmRuntimeException(errorMsg);
        } else {
            return e;
        }
    }

    @Override
    public void initPassiveSegment(int segmentId, int dest, int offset, int size) {
        var segment = dataSegments[segmentId];
        write(dest, segment.data(), offset, size);
    }

    private int sizeInBytes() {
        return PAGE_SIZE * nPages;
    }

    @Override
    public void write(int addr, byte[] data, int offset, int size) {
        try {
            buffer.position(addr);
            buffer.put(data, offset, size);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, size, sizeInBytes());
        }
    }

    @Override
    public byte read(int addr) {
        try {
            return buffer.get(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 1, sizeInBytes());
        }
    }

    @Override
    public byte[] readBytes(int addr, int len) {
        try {
            var bytes = new byte[len];
            buffer.position(addr);
            buffer.get(bytes);
            return bytes;
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, len, sizeInBytes());
        }
    }

    @Override
    public void writeI32(int addr, int data) {
        try {
            buffer.putInt(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 4, sizeInBytes());
        }
    }

    @Override
    public int readInt(int addr) {
        try {
            return buffer.getInt(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 4, sizeInBytes());
        }
    }

    @Override
    public void writeLong(int addr, long data) {
        try {
            buffer.putLong(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 8, sizeInBytes());
        }
    }

    @Override
    public long readLong(int addr) {
        try {
            return buffer.getLong(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 8, sizeInBytes());
        }
    }

    @Override
    public void writeShort(int addr, short data) {
        try {
            buffer.putShort(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 2, sizeInBytes());
        }
    }

    @Override
    public short readShort(int addr) {
        try {
            return buffer.getShort(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 2, sizeInBytes());
        }
    }

    @Override
    public long readU16(int addr) {
        try {
            return buffer.getShort(addr) & 0xffff;
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 2, sizeInBytes());
        }
    }

    @Override
    public void writeByte(int addr, byte data) {
        try {
            buffer.put(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 1, sizeInBytes());
        }
    }

    @Override
    public void writeF32(int addr, float data) {
        try {
            buffer.putFloat(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 4, sizeInBytes());
        }
    }

    @Override
    public long readF32(int addr) {
        try {
            return buffer.getInt(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 4, sizeInBytes());
        }
    }

    @Override
    public float readFloat(int addr) {
        try {
            return buffer.getFloat(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 4, sizeInBytes());
        }
    }

    @Override
    public void writeF64(int addr, double data) {
        try {
            buffer.putDouble(addr, data);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 8, sizeInBytes());
        }
    }

    @Override
    public double readDouble(int addr) {
        try {
            return buffer.getDouble(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 8, sizeInBytes());
        }
    }

    @Override
    public long readF64(int addr) {
        try {
            return buffer.getLong(addr);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, addr, 8, sizeInBytes());
        }
    }

    @Override
    public void zero() {
        fill((byte) 0, 0, sizeInBytes());
    }

    @Override
    @SuppressWarnings("ByteBufferBackingArray")
    public void fill(byte value, int fromIndex, int toIndex) {
        // see https://appsintheopen.com/posts/53-resetting-bytebuffers-to-zero-in-java
        try {
            Arrays.fill(buffer.array(), fromIndex, toIndex, value);
            buffer.position(0);
        } catch (RuntimeException e) {
            throw outOfBoundsException(e, fromIndex, toIndex - fromIndex, sizeInBytes());
        }
    }

    @Override
    public void drop(int segment) {
        dataSegments[segment] = PassiveDataSegment.EMPTY;
    }
}
