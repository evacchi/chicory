package com.dylibso.chicory.testing;

import com.dylibso.chicory.runtime.*;
import com.dylibso.chicory.wasm.Module;
import com.dylibso.chicory.wasm.types.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store {
    private List<HostFunction> functions;
    private List<HostGlobal> globals;
    private List<HostMemory> memories;
    private List<HostTable> tables;

    Store() {}

    public Store withFunctions(List<HostFunction> functions) {
        this.functions = functions;
        return this;
    }

    public Store addFunction(HostFunction... function) {
        if (this.functions == null) {
            this.functions = new ArrayList<>();
        }
        Collections.addAll(this.functions, function);
        return this;
    }

    public Store withGlobals(List<HostGlobal> globals) {
        this.globals = globals;
        return this;
    }

    public Store addGlobal(HostGlobal... global) {
        if (this.globals == null) {
            this.globals = new ArrayList<>();
        }
        Collections.addAll(this.globals, global);
        return this;
    }

    public Store withMemories(List<HostMemory> memories) {
        this.memories = memories;
        return this;
    }

    public Store addMemory(HostMemory... memory) {
        if (this.memories == null) {
            this.memories = new ArrayList<>();
        }
        Collections.addAll(this.memories, memory);
        return this;
    }

    public Store withTables(List<HostTable> tables) {
        this.tables = tables;
        return this;
    }

    public Store addTable(HostTable... table) {
        if (this.tables == null) {
            this.tables = new ArrayList<>();
        }
        Collections.addAll(this.tables, table);
        return this;
    }

    private HostImports toHostImports() {
        final HostImports hostImports =
                new HostImports(
                        functions == null
                                ? new HostFunction[0]
                                : functions.toArray(new HostFunction[0]),
                        globals == null ? new HostGlobal[0] : globals.toArray(new HostGlobal[0]),
                        memories == null ? new HostMemory[0] : memories.toArray(new HostMemory[0]),
                        tables == null ? new HostTable[0] : tables.toArray(new HostTable[0]));
        return hostImports;
    }

    public Instance instantiate(String name, Module m) {
        HostImports hostImports = toHostImports();
        Instance instance = Instance.builder(m).withHostImports(hostImports).build();

        ExportSection exportSection = m.exportSection();
        for (int i = 0; i < exportSection.exportCount(); i++) {
            Export export = exportSection.getExport(i);
            String exportName = export.name();
            switch (export.exportType()) {
                case FUNCTION:
                    ExportFunction f = instance.export(exportName);
                    FunctionType ftype = instance.exportType(exportName);
                    this.addFunction(
                            new HostFunction(
                                    (inst, args) -> f.apply(args),
                                    name,
                                    exportName,
                                    ftype.params(),
                                    ftype.returns()));
                    break;

                case TABLE:
                    this.addTable(new HostTable(name, exportName, instance.table(export.index())));
                    break;

                case MEMORY:
                    this.addMemory(new HostMemory(name, exportName, instance.memory()));
                    break;

                case GLOBAL:
                    GlobalInstance g = instance.global(export.index());
                    this.addGlobal(new HostGlobal(name, exportName, g));
                    break;
            }
        }

        return instance;
    }
}
