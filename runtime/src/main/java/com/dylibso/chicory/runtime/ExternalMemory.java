package com.dylibso.chicory.runtime;

public class ExternalMemory implements ExternalValue {
    private final String moduleName;
    private final String symbolName;
    private final Memory memory;

    public ExternalMemory(String moduleName, String symbolName, Memory memory) {
        this.moduleName = moduleName;
        this.symbolName = symbolName;
        this.memory = memory;
    }

    @Override
    public String moduleName() {
        return moduleName;
    }

    @Override
    public String symbolName() {
        return symbolName;
    }

    @Override
    public ExternalValue.Type type() {
        return Type.MEMORY;
    }

    public Memory memory() {
        return memory;
    }
}
