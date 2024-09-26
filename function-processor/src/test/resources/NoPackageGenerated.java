import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.dylibso.chicory.function.processor.FunctionProcessor")
public final class NoPackage_ModuleFactory {

    private NoPackage_ModuleFactory() {
    }

    public static HostFunction[] toHostFunctions(NoPackage functions) {
        return new HostFunction[] { //
                new HostFunction("nopackage",
                        "print",
                        (Instance instance, long... args) -> {
                            functions.print(instance.memory(),
                                    (int) args[0],
                                    (int) args[1]);
                            return null;
                        },
                        List.of(ValueType.I32,
                                ValueType.I32),
                        List.of()), //
                new HostFunction("nopackage",
                        "exit",
                        (Instance instance, long... args) -> {
                            functions.exit();
                            return null;
                        },
                        List.of(),
                        List.of()) };
    }
}