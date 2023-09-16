package com.dylibso.chicory.runtime;

import com.dylibso.chicory.wasm.types.*;

import java.util.*;

/**
 * This is responsible for holding and interpreting the Wasm code.
 */
public class Machine {
    private MStack stack;
    private Stack<StackFrame> callStack;
    private Instance instance;

    public Machine(Instance instance) {
        this.instance = instance;
        this.stack = new MStack();
        this.callStack = new Stack<>();
    }

    public Value call(int funcId, Value[] args, boolean popResults) throws ChicoryException {
        var func = instance.getFunction(funcId);
        if (func != null) {
            this.callStack.push(new StackFrame(funcId, 0, args, func.getLocals()));
            eval(func.getInstructions());
        } else {
            this.callStack.push(new StackFrame(funcId, 0, args, List.of()));
            var imprt = instance.getImports()[funcId];
            var hostFunc = imprt.handle();
            var results = hostFunc.apply(this.instance.getMemory(), args);
            // a host function can return null or an array of ints
            // which we will push onto the stack
            if (results != null) {
                for (var result : results) {
                    this.stack.push(result);
                }
            }
        }
        if (this.callStack.size() > 0) this.callStack.pop();
        if (!popResults) {
            return null;
        }

        var typeId = instance.getFunctionTypes()[funcId];
        var type = instance.getTypes()[typeId];
        if (type.getReturns().length == 0) return null;
        if (this.stack.size() == 0) return null;
        return this.stack.pop();
    }

    void eval(List<Instruction> code) throws ChicoryException {
        var frame = callStack.peek();
        boolean shouldReturn = false;
        loop: while (frame.pc < code.size()) {
            if (shouldReturn) return;
            var instruction = code.get(frame.pc++);
            var opcode = instruction.getOpcode();
            var operands = instruction.getOperands();
            //System.out.println("func="+frame.funcId + "@"+frame.pc + ": " + instruction + " stack="+this.stack);
            switch (opcode) {
                case UNREACHABLE -> throw new TrapException("Trapped on unreachable instruction", callStack);
                case NOP -> {}
                case LOOP, BLOCK -> {
                    frame.blockDepth++;
                }
                case IF -> {
                    frame.blockDepth++;
                    var pred = this.stack.pop().asInt();
                    if (pred == 0) {
                        frame.pc = instruction.labelFalse;
                    } else {
                        frame.pc = instruction.labelTrue;
                    }
                }
                case ELSE, BR -> {
                    frame.pc = instruction.labelTrue;
                }
                case BR_IF -> {
                    var pred = this.stack.pop().asInt();
                    if (pred == 0) {
                        frame.pc = instruction.labelFalse;
                    } else {
                        frame.pc = instruction.labelTrue;
                    }
                }
                case BR_TABLE -> {
                    var pred = this.stack.pop().asInt();
                    if (pred < 0 || pred >= instruction.labelTable.length - 1) {
                        // choose default
                        frame.pc = instruction.labelTable[instruction.labelTable.length - 1];
                    } else {
                        frame.pc = instruction.labelTable[pred];
                    }
                }
                case RETURN -> shouldReturn = true;
                case CALL_INDIRECT -> {
//                    var index = this.stack.pop().asInt();
//                    var funcId = instance.getTable().getFuncRef(index);
//                    var typeId = instance.getFunctionTypes().get(funcId);
//                    var type = instance.getTypes().get(typeId);
//                    // given a list of param types, let's pop those params off the stack
//                    // and pass as args to the function call
//                    var args = extractArgsForParams(type.paramTypes());
//                    call(funcId, args, false);
                }
                case DROP -> this.stack.pop();
                case SELECT -> {
                    var pred = this.stack.pop().asInt();
                    var b = this.stack.pop();
                    var a = this.stack.pop();
                    if (pred == 0) {
                        this.stack.push(b);
                    } else {
                        this.stack.push(a);
                    }
                }
                case END -> {
                    frame.blockDepth--;
                    // if this is the last end, then we're done with
                    // the function
                    if (frame.blockDepth == 0) {
                        break loop;
                    }
                }
                case LOCAL_GET -> {
                    this.stack.push(frame.getLocal((int) operands[0]));
                }
                case LOCAL_SET -> {
                    frame.setLocal((int) operands[0], this.stack.pop());
                }
                case LOCAL_TEE -> {
                    // here we peek instead of pop, leaving it on the stack
                    frame.setLocal((int) operands[0], this.stack.peek());
                }
                case GLOBAL_GET -> {
                    var val = instance.getGlobal((int) operands[0]);
                    this.stack.push(val);
                }
                case GLOBAL_SET -> {
                    var id = (int) operands[0];
                    var global = instance.getGlobalInitalizers()[id];
                    if (global.getMutabilityType() == MutabilityType.Const) throw new RuntimeException("Can't call GLOBAL_SET on immutable global");
                    var val = this.stack.pop();
                    instance.setGlobal(id, val);
                }
                // TODO signed and unsigned are the same right now
                case I32_LOAD -> {
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    var val = instance.getMemory().getI32(ptr);
                    this.stack.push(val);
                }
                case I64_LOAD -> {
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    var val = instance.getMemory().getI64(ptr);
                    this.stack.push(val);
                }
                case I32_LOAD8_S -> {
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    var val = instance.getMemory().getI8(ptr);
                    this.stack.push(val);
                }
                case I32_LOAD8_U -> {
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    var val = instance.getMemory().getI8U(ptr);
                    this.stack.push(val);
                }
                case I32_LOAD16_S -> {
                    // TODO why though
                    // we add 2 because the bytes we want are at the end
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    var val = instance.getMemory().getI16(ptr);
                    this.stack.push(val);
                }
                case I32_STORE -> {
                    var value = this.stack.pop().asInt();
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    instance.getMemory().putI32(ptr, value);
                }
                case I64_STORE -> {
                    var value = this.stack.pop().asLong();
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    instance.getMemory().putI64(ptr, value);
                }
                case MEMORY_GROW -> {
                    instance.getMemory().grow();
                }
                case I32_STORE8 -> {
                    var value = this.stack.pop().asByte();
                    var ptr = (int) (operands[0] + this.stack.pop().asInt());
                    instance.getMemory().putByte(ptr, value);
                }
                case MEMORY_SIZE -> {
                    var sz = instance.getMemory().getInitialSize();
                    this.stack.push(Value.i32(sz));
                }
                // TODO 32bit and 64 bit operations are the same for now
                case I32_CONST -> {
                    this.stack.push(Value.i32(operands[0]));
                }
                case I64_CONST -> {
                    this.stack.push(Value.i64(operands[0]));
                }
                case I32_EQ -> {
                    var a = stack.pop().asInt();
                    var b = stack.pop().asInt();
                    this.stack.push(a == b ? Value.TRUE : Value.FALSE);
                }
                case I64_EQ -> {
                    var a = this.stack.pop().asLong();
                    var b = this.stack.pop().asLong();
                    this.stack.push(a == b ? Value.TRUE : Value.FALSE);
                }
                case I32_NE -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(a == b ? Value.FALSE : Value.TRUE);
                }
                case I64_NE -> {
                    var a = this.stack.pop().asLong();
                    var b = this.stack.pop().asLong();
                    this.stack.push(a == b ? Value.FALSE : Value.TRUE);
                }
                case I32_EQZ -> {
                    var a = this.stack.pop().asInt();
                    this.stack.push(a == 0 ? Value.TRUE : Value.FALSE);
                }
                case I64_EQZ -> {
                    var a = this.stack.pop().asLong();
                    this.stack.push(a == 0L ? Value.TRUE : Value.FALSE);
                }
                case I32_LT_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(a < b ? Value.TRUE : Value.FALSE);
                }
                case I32_LT_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(a < b ? Value.TRUE : Value.FALSE);
                }
                // TODO split
                case I64_LT_S, I64_LT_U -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(a < b ? Value.TRUE : Value.FALSE);
                }
                case I32_GT_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(a > b ? Value.TRUE : Value.FALSE);
                }
                case I32_GT_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(a > b ? Value.TRUE : Value.FALSE);
                }
                // TODO split
                case I64_GT_S, I64_GT_U -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(a > b ? Value.TRUE : Value.FALSE);
                }
                case I32_GE_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(a >= b ? Value.TRUE : Value.FALSE);
                }
                case I32_GE_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(a >= b ? Value.TRUE : Value.FALSE);
                }
                // TODO split
                case I64_GE_U, I64_GE_S -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(a >= b ? Value.TRUE : Value.FALSE);
                }
                case I32_LE_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(a <= b ? Value.TRUE : Value.FALSE);
                }
                case I32_LE_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(a <= b ? Value.TRUE : Value.FALSE);
                }
                case I64_LE_U, I64_LE_S -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(a <= b ? Value.TRUE : Value.FALSE);
                }
                case I32_CLZ -> {
                    var count = this.stack.pop().i32CLZ();
                    this.stack.push(Value.i32(count));
                }
                case I32_CTZ -> {
                    var count = this.stack.pop().i32CTZ();
                    this.stack.push(Value.i32(count));
                }
                case I32_POPCNT -> {
                    var count = this.stack.pop().popCount();
                    this.stack.push(Value.i32(count));
                }
                case I32_ADD -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a + b));
                }
                case I64_ADD -> {
                    var a = this.stack.pop().asLong();
                    var b = this.stack.pop().asLong();
                    this.stack.push(Value.i64(a + b));
                }
                case I32_SUB -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(b - a));
                }
                case I64_SUB -> {
                    var a = this.stack.pop().asLong();
                    var b = this.stack.pop().asLong();
                    this.stack.push(Value.i64(b - a));
                }
                case I32_MUL -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a * b));
                }
                case I64_MUL -> {
                    var a = this.stack.pop().asLong();
                    var b = this.stack.pop().asLong();
                    this.stack.push(Value.i64(a * b));
                }
                case I32_DIV_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a / b));
                }
                case I32_DIV_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(Value.i32(a / b));
                }
                // TODO split up
                case I64_DIV_S, I64_DIV_U -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(Value.i64(a / b));
                }
                case I32_REM_S -> {
                    var b = this.stack.pop().asInt();
                    var a = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a % b));
                }
                case I32_REM_U -> {
                    var b = this.stack.pop().asUInt();
                    var a = this.stack.pop().asUInt();
                    this.stack.push(Value.i32(a % b));
                }
                // TODO split up
                case I64_REM_S, I64_REM_U -> {
                    var b = this.stack.pop().asLong();
                    var a = this.stack.pop().asLong();
                    this.stack.push(Value.i64(a % b));
                }
                case CALL -> {
                    var funcId = (int) operands[0];
                    var typeId = instance.getFunctionTypes()[funcId];
                    var type = instance.getTypes()[typeId];
                    // given a list of param types, let's pop those params off the stack
                    // and pass as args to the function call
                    var args = extractArgsForParams(type.getParams());
                    call(funcId, args, false);
                }
                case I32_AND -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a & b));
                }
                case I32_OR -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a | b));
                }
                case I32_XOR -> {
                    var a = this.stack.pop().asInt();
                    var b = this.stack.pop().asInt();
                    this.stack.push(Value.i32(a ^ b));
                }
                case I32_SHL -> {
                    var c = this.stack.pop().asInt();
                    var v = this.stack.pop().asInt();
                    this.stack.push(Value.i32(v << c));
                }
                case I32_SHR_S -> {
                    var c = this.stack.pop().asInt();
                    var v = this.stack.pop().asInt();
                    this.stack.push(Value.i32(v >> c));
                }
                case I32_SHR_U -> {
                    var c = this.stack.pop().asInt();
                    var v = this.stack.pop().asInt();
                    this.stack.push(Value.i32(v >>> c));
                }
                case I32_ROTL -> {
                    var c = this.stack.pop().asInt();
                    var v = this.stack.pop().asInt();
                    var z = (v << c) | (v >>> (32 - c));
                    this.stack.push(Value.i32(z));
                }
                case I32_ROTR -> {
                    var c = this.stack.pop().asInt();
                    var v = this.stack.pop().asInt();
                    var z = (v >>> c) | (v << (32 - c));
                    this.stack.push(Value.i32(z));
                }
                case I32_EXTEND_8_S -> {
                    int tos = this.stack.pop().asInt();
                    int result = tos << 24 >> 24;
                    this.stack.push(Value.i32(result));
                }
                case I32_EXTEND_16_S -> {
                    int original = this.stack.pop().asInt() & 0xFFFF;
                    if ((original & 0x8000) != 0) original |= 0xFFFF0000;
                    this.stack.push(Value.i32(original & 0xFFFFFFFFL));
                }
                default -> throw new RuntimeException("Machine doesn't recognize Instruction " + instruction);
            }
        }
    }

    public void printStackTrace() {
        System.out.println("Trapped. Stacktrace:");
        for (var f : callStack) {
            System.out.println(f);
        }
    }

    Value[] extractArgsForParams(ValueType[] params) {
        if (params == null) return new Value[]{};
        var args = new Value[params.length];
        for (var i = 0; i < params.length; i++) {
            var p = this.stack.pop();
            var t = params[i];
            if (p.getType() != t) {
                throw new RuntimeException("Type error when extracting args.");
            }
            args[i] = p;
        }
        return args;
    }
}
