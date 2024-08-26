package com.dylibso.chicory.imports;

import static com.dylibso.chicory.testing.COPY_SpecV1LinkingTest.MgInstance;
import static com.dylibso.chicory.testing.COPY_SpecV1LinkingTest.MmInstance;
import static com.dylibso.chicory.testing.COPY_SpecV1LinkingTest.MsInstance;
import static com.dylibso.chicory.testing.COPY_SpecV1LinkingTest.MtInstance;

import com.dylibso.chicory.runtime.*;
import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.test.gen.SpecV1LinkingTest;
import com.dylibso.chicory.wasm.types.*;

import java.util.List;

public class SpecV1LinkingHostFuncs {

    public static HostImports.Builder hostImportBuilder =
            HostImports.builder().addFunction(
                    new HostFunction(
                            (Instance instance, Value... args) -> {
                                return null;
                            },
                            "spectest",
                            "print_i32",
                            List.of(ValueType.I32),
                            List.of()));

    public static HostImports Mf() {
        hostImportBuilder.addFunction(new HostFunction(
                (Instance instance, Value... args) -> new Value[] {Value.i32(2)},
                "Mf",
                "call",
                List.of(),
                List.of(ValueType.I32)));
        return hostImportBuilder.build();
    }

    public static HostImports Nf() {
        return hostImportBuilder.build();
    }

    public static HostImports Mg() {
        return hostImportBuilder.build();
    }

    public static HostImports Mt() {
        return hostImportBuilder.build();
    }

    public static HostImports Nt() {
        return hostImportBuilder
                .addFunction(new HostFunction(
                        (Instance instance, Value... args) -> {
                            return MtInstance.export("call").apply(args);
                        },
                        "Mt",
                        "call",
                        List.of(ValueType.I32),
                        List.of(ValueType.I32)))
                .addFunction(new HostFunction(
                        (Instance instance1, Value... args1) -> {
                            return MtInstance.export("h").apply(args1);
                        },
                        "Mt",
                        "h",
                        List.of(),
                        List.of(ValueType.I32))).build();
    }

    public static HostImports Ng() {
        return hostImportBuilder
                .addFunction(
                        new HostFunction(
                                (Instance instance, Value... args) ->
                                        new Value[] {MgInstance.readGlobal(0)},
                                "Mg",
                                "get",
                                List.of(),
                                List.of(ValueType.I32)),
                        new HostFunction(
                                (Instance instance, Value... args) ->
                                        new Value[] {MgInstance.readGlobal(1)},
                                "Mg",
                                "get_mut",
                                List.of(),
                                List.of(ValueType.I32)),
                        new HostFunction(
                                (Instance instance, Value... args) -> {
                                    MgInstance.writeGlobal(1, args[0]);
                                    return null;
                                },
                                "Mg",
                                "set_mut",
                                List.of(ValueType.I32),
                                List.of()))
                .addGlobal(
                        new HostGlobal("Mg", "glob", MgInstance.global(0)),
                        new HostGlobal("Mg", "mut_glob", MgInstance.global(1), MutabilityType.Var))
                .build();
    }

    private static HostTable MtTab() {
        return new HostTable("Mt", "tab", MtInstance.table(0));
    }

    public static HostImports Ot() {
        return hostImportBuilder
                //.addFunction(Mth()) // already added earlier as a side-effect
                .addTable(MtTab()).build();
    }

    public static HostImports testModule10() {
        return hostImportBuilder.addTable(MtTab()).build();
    }

    public static HostImports G2() {
        return hostImportBuilder
                .addGlobal(new HostGlobal("G1", "g", new GlobalInstance(Value.i32(5))))
                .build();
    }

    public static HostImports Mm() {
        return hostImportBuilder
//                .addMemory(new HostMemory("Mm", "mem", new Memory(new MemoryLimits(1, 5)))) // FIXME this does not work correctly
                .addFunction(
                        new HostFunction(
                                (Instance instance, Value... args) -> {
                                    return MmInstance.export("load").apply(args);
                                },
                                "Mm",
                                "load",
                                List.of(ValueType.I32),
                                List.of(ValueType.I32)))
                .build();
    }

    public static HostImports Om() {
        return hostImportBuilder.build();
    }

    public static HostImports testModule18() {
        return hostImportBuilder.build();
    }

    public static HostImports Pm() {
        return hostImportBuilder.build();
    }


    public static HostImports Nm() {
        return hostImportBuilder
                // FIXME: this should be registered upon instantiation of Mm
                .addMemory(new HostMemory("Mm", "mem", MmInstance.memory()))
                .build();
    }

    public static HostImports Ms() {
        return hostImportBuilder
                .build();
    }

    public static HostImports Mref_im() {
        return hostImportBuilder
                .addGlobal(
                        new HostGlobal(
                                "Mref_ex", "g-const-func", new GlobalInstance(Value.funcRef(0))))
                .addGlobal(
                        new HostGlobal(
                                "Mref_ex",
                                "g-const-extern",
                                new GlobalInstance(Value.externRef(0))))
                .addGlobal(
                        new HostGlobal(
                                "Mref_ex",
                                "g-var-func",
                                new GlobalInstance(Value.funcRef(0)),
                                MutabilityType.Var))
                .addGlobal(
                        new HostGlobal(
                                "Mref_ex",
                                "g-var-extern",
                                new GlobalInstance(Value.externRef(0)),
                                MutabilityType.Var))
                .build();
    }

    static int STATE = 0;

    public static HostImports fallback() {
        switch (STATE) {
            case 1:
                hostImportBuilder.addFunction(
                        new HostFunction(
                                (Instance instance, Value... args) -> {
                                    return null;
                                },
                                "reexport_f",
                                "print",
                                List.of(),
                                List.of()));

                break;
            case 3:
                hostImportBuilder
                        .addTable(
                                new HostTable(
                                        "Mtable_ex",
                                        "t-func",
                                        new TableInstance(
                                                new Table(ValueType.FuncRef, new Limits(1)))))
                        .addTable(
                                new HostTable(
                                        "Mtable_ex",
                                        "t-extern",
                                        new TableInstance(
                                                new Table(ValueType.ExternRef, new Limits(1)))));
                break;
            case 23:
                hostImportBuilder
                        .addMemory(new HostMemory("Ms", "memory", MsInstance.memory()))
                        .addTable(new HostTable("Ms", "table", MsInstance.table(0)));


        }

        ++STATE;


        return hostImportBuilder.build();

    }
}
