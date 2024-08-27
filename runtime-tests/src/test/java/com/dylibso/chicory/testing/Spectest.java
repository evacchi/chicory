package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;

import java.util.List;

public class Spectest {
    HostFunction testFunc =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func",
                    List.of(),
                    List.of());
    HostFunction testFuncI32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func-i32",
                    List.of(ValueType.I32),
                    List.of());
    HostFunction testFuncToI32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func->i32",
                    List.of(),
                    List.of(ValueType.I32));
    HostFunction testFuncI32ToI32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func-i32->i32",
                    List.of(ValueType.I32),
                    List.of(ValueType.I32));
    HostFunction testFuncI64 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return new Value[] {args[0]};
                    },
                    "test",
                    "func-i64->i64",
                    List.of(ValueType.I64),
                    List.of(ValueType.I64));
    HostFunction testFuncF32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func-f32",
                    List.of(ValueType.F32),
                    List.of());
    HostFunction testFuncToF32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "test",
                    "func->f32",
                    List.of(),
                    List.of(ValueType.F32));
    HostFunction printI32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i32",
                    List.of(ValueType.I32),
                    List.of());
    HostFunction printI32_1 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i32_1",
                    List.of(ValueType.I32),
                    List.of());
    HostFunction printI32_2 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i32_2",
                    List.of(ValueType.I32),
                    List.of());
    HostFunction printF32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_f32",
                    List.of(ValueType.F32),
                    List.of());
    HostFunction printI32F32 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i32_f32",
                    List.of(ValueType.I32, ValueType.F32),
                    List.of());
    HostFunction printI64 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i64",
                    List.of(ValueType.I64),
                    List.of());
    HostFunction printI64_1 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i64_1",
                    List.of(ValueType.I64),
                    List.of());
    HostFunction printI64_2 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_i64_2",
                    List.of(ValueType.I64),
                    List.of());
    HostFunction printF64 =
            new HostFunction(
                    (Instance instance, Value... args) -> {
                        return null;
                    },
                    "spectest",
                    "print_f64",
                    List.of(ValueType.F64),
                    List.of());
    HostFunction printF64F64 =
            new HostFunction(
                    this::noop,
                    "spectest",
                    "print_f64_f64",
                    List.of(ValueType.F64, ValueType.F64),
                    List.of());

    private Value[] noop(Instance inst, Value... args) {}
}
