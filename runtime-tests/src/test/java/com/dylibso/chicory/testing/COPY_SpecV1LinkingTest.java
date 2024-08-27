package com.dylibso.chicory.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.exceptions.ChicoryException;
import com.dylibso.chicory.wasm.exceptions.UninstantiableException;
import com.dylibso.chicory.wasm.exceptions.UnlinkableException;
import com.dylibso.chicory.wasm.types.Value;
import com.dylibso.chicory.wasm.types.ValueType;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class COPY_SpecV1LinkingTest {

    public static Store store =
            new Store()
                    .addFunction(
                            new HostFunction(
                                    (Instance instance, Value... args) -> {
                                        return null;
                                    },
                                    "spectest",
                                    "print_i32",
                                    List.of(ValueType.I32),
                                    List.of()));

    public static Instance MfInstance = null;

    @Test()
    @Order(0)
    public void instantiate_MfInstance() {
        MfInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.0.wasm"))
                        .instantiate(store, "Mf");
    }

    public static Instance NfInstance = null;

    @Test()
    @Order(1)
    public void instantiate_NfInstance() {
        NfInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.1.wasm"))
                        .instantiate(store, "Nf");
    }

    @Test()
    @Order(2)
    public void test2() {
        ExportFunction varCall = MfInstance.export("call");
        var results = varCall.apply();
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    @Test()
    @Order(3)
    public void test3() {
        ExportFunction varMfCall = NfInstance.export("Mf.call");
        var results = varMfCall.apply();
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    @Test()
    @Order(4)
    public void test4() {
        ExportFunction varCall = NfInstance.export("call");
        var results = varCall.apply();
        assertEquals(Integer.parseUnsignedInt("3"), results[0].asInt());
    }

    @Test()
    @Order(5)
    public void test5() {
        ExportFunction varCallMfCall = NfInstance.export("call Mf.call");
        var results = varCallMfCall.apply();
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    public static Instance testModule2Instance = null;

    @Test()
    @Order(6)
    public void instantiate_testModule2Instance() {
        testModule2Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.2.wasm"))
                        .instantiate(store, "reexport_f");
    }

    @Test()
    @Order(7)
    public void test7() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.3.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(8)
    public void test8() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.4.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance MgInstance = null;

    @Test()
    @Order(9)
    public void instantiate_MgInstance() {
        MgInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.5.wasm"))
                        .instantiate(store, "Mg");
    }

    public static Instance NgInstance = null;

    @Test()
    @Order(10)
    public void instantiate_NgInstance() {
        NgInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.6.wasm"))
                        .instantiate(store, "Ng");
    }

    @Test()
    @Order(11)
    public void test11() {
        ExportFunction varGlob = MgInstance.export("glob");
        var results = varGlob.apply();
        assertEquals(Integer.parseUnsignedInt("42"), results[0].asInt());
    }

    @Test()
    @Order(12)
    public void test12() {
        ExportFunction varMgGlob = NgInstance.export("Mg.glob");
        var results = varMgGlob.apply();
        assertEquals(Integer.parseUnsignedInt("42"), results[0].asInt());
    }

    @Test()
    @Order(13)
    public void test13() {
        ExportFunction varGlob = NgInstance.export("glob");
        var results = varGlob.apply();
        assertEquals(Integer.parseUnsignedInt("43"), results[0].asInt());
    }

    @Test()
    @Order(14)
    public void test14() {
        ExportFunction varGet = MgInstance.export("get");
        var results = varGet.apply();
        assertEquals(Integer.parseUnsignedInt("42"), results[0].asInt());
    }

    @Test()
    @Order(15)
    public void test15() {
        ExportFunction varMgGet = NgInstance.export("Mg.get");
        var results = varMgGet.apply();
        assertEquals(Integer.parseUnsignedInt("42"), results[0].asInt());
    }

    @Test()
    @Order(16)
    public void test16() {
        ExportFunction varGet = NgInstance.export("get");
        var results = varGet.apply();
        assertEquals(Integer.parseUnsignedInt("43"), results[0].asInt());
    }

    @Test()
    @Order(17)
    public void test17() {
        ExportFunction varMutGlob = MgInstance.export("mut_glob");
        var results = varMutGlob.apply();
        assertEquals(Integer.parseUnsignedInt("142"), results[0].asInt());
    }

    @Test()
    @Order(18)
    public void test18() {
        ExportFunction varMgMutGlob = NgInstance.export("Mg.mut_glob");
        var results = varMgMutGlob.apply();
        assertEquals(Integer.parseUnsignedInt("142"), results[0].asInt());
    }

    @Test()
    @Order(19)
    public void test19() {
        ExportFunction varGetMut = MgInstance.export("get_mut");
        var results = varGetMut.apply();
        assertEquals(Integer.parseUnsignedInt("142"), results[0].asInt());
    }

    @Test()
    @Order(20)
    public void test20() {
        ExportFunction varMgGetMut = NgInstance.export("Mg.get_mut");
        var results = varMgGetMut.apply();
        assertEquals(Integer.parseUnsignedInt("142"), results[0].asInt());
    }

    @Test()
    @Order(21)
    public void test21() {
        ExportFunction varSetMut = MgInstance.export("set_mut");
        var results = varSetMut.apply(Value.i32(Integer.parseUnsignedInt("241")));
    }

    @Test()
    @Order(22)
    public void test22() {
        ExportFunction varMutGlob = MgInstance.export("mut_glob");
        var results = varMutGlob.apply();
        assertEquals(Integer.parseUnsignedInt("241"), results[0].asInt());
    }

    @Test()
    @Order(23)
    public void test23() {
        ExportFunction varMgMutGlob = NgInstance.export("Mg.mut_glob");
        var results = varMgMutGlob.apply();
        assertEquals(Integer.parseUnsignedInt("241"), results[0].asInt());
    }

    @Test()
    @Order(24)
    public void test24() {
        ExportFunction varGetMut = MgInstance.export("get_mut");
        var results = varGetMut.apply();
        assertEquals(Integer.parseUnsignedInt("241"), results[0].asInt());
    }

    @Test()
    @Order(25)
    public void test25() {
        ExportFunction varMgGetMut = NgInstance.export("Mg.get_mut");
        var results = varMgGetMut.apply();
        assertEquals(Integer.parseUnsignedInt("241"), results[0].asInt());
    }

    @Test()
    @Order(26)
    public void test26() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.7.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(27)
    public void test27() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.8.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance Mref_exInstance = null;

    @Test()
    @Order(28)
    public void instantiate_Mref_exInstance() {
        Mref_exInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.9.wasm"))
                        .instantiate(store, "Mref_ex");
    }

    public static Instance Mref_imInstance = null;

    @Test()
    @Order(29)
    public void instantiate_Mref_imInstance() {
        Mref_imInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.10.wasm"))
                        .instantiate(store, "Mref_im");
    }

    @Test()
    @Order(30)
    public void test30() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.11.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(31)
    public void test31() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.12.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(32)
    public void test32() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.13.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(33)
    public void test33() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.14.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance MtInstance = null;

    @Test()
    @Order(34)
    public void instantiate_MtInstance() {
        MtInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.15.wasm"))
                        .instantiate(store, "Mt");
    }

    public static Instance NtInstance = null;

    @Test()
    @Order(35)
    public void instantiate_NtInstance() {
        NtInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.16.wasm"))
                        .instantiate(store, "Nt");
    }

    @Test()
    @Order(36)
    public void test36() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(37)
    public void test37() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var results = varMtCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(38)
    public void test38() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(39)
    public void test39() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var results = varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(40)
    public void test40() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("1"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(41)
    public void test41() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varMtCall.apply(Value.i32(Integer.parseUnsignedInt("1"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(42)
    public void test42() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(43)
    public void test43() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("1"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(44)
    public void test44() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(45)
    public void test45() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varMtCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(46)
    public void test46() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(47)
    public void test47() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(48)
    public void test48() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("20"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    @Test()
    @Order(49)
    public void test49() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varMtCall.apply(Value.i32(Integer.parseUnsignedInt("20"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    @Test()
    @Order(50)
    public void test50() {
        ExportFunction varCall = NtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("7"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    @Test()
    @Order(51)
    public void test51() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("20"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    @Test()
    @Order(52)
    public void test52() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("3")));
        assertEquals(Integer.parseUnsignedInt("4294967292"), results[0].asInt());
    }

    @Test()
    @Order(53)
    public void test53() {
        ExportFunction varCall = NtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("4"))));
        assertTrue(
                exception.getMessage().contains("indirect call type mismatch"),
                "'" + exception.getMessage() + "' doesn't contains: 'indirect call type mismatch");
    }

    public static Instance OtInstance = null;

    @Test()
    @Order(54)
    public void instantiate_OtInstance() {
        OtInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.17.wasm"))
                        .instantiate(store, "Ot");
    }

    @Test()
    @Order(55)
    public void test55() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("3")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(56)
    public void test56() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var results = varMtCall.apply(Value.i32(Integer.parseUnsignedInt("3")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(57)
    public void test57() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var results = varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("3")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(58)
    public void test58() {
        ExportFunction varCall = OtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("3")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(59)
    public void test59() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4294967292"), results[0].asInt());
    }

    @Test()
    @Order(60)
    public void test60() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var results = varMtCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4294967292"), results[0].asInt());
    }

    @Test()
    @Order(61)
    public void test61() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(62)
    public void test62() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var results = varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4294967292"), results[0].asInt());
    }

    @Test()
    @Order(63)
    public void test63() {
        ExportFunction varCall = OtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("4294967292"), results[0].asInt());
    }

    @Test()
    @Order(64)
    public void test64() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("6"), results[0].asInt());
    }

    @Test()
    @Order(65)
    public void test65() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var results = varMtCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("6"), results[0].asInt());
    }

    @Test()
    @Order(66)
    public void test66() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(67)
    public void test67() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var results = varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("6"), results[0].asInt());
    }

    @Test()
    @Order(68)
    public void test68() {
        ExportFunction varCall = OtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("6"), results[0].asInt());
    }

    @Test()
    @Order(69)
    public void test69() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(70)
    public void test70() {
        ExportFunction varMtCall = NtInstance.export("Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varMtCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(71)
    public void test71() {
        ExportFunction varCall = NtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(72)
    public void test72() {
        ExportFunction varCallMtCall = NtInstance.export("call Mt.call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCallMtCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(73)
    public void test73() {
        ExportFunction varCall = OtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(74)
    public void test74() {
        ExportFunction varCall = OtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("20"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    public static Instance testModule10Instance = null;

    @Test()
    @Order(75)
    public void instantiate_testModule10Instance() {
        testModule10Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.18.wasm"))
                        .instantiate(store, "__ignore");
    }

    public static Instance G1Instance = null;

    @Test()
    @Order(76)
    public void instantiate_G1Instance() {
        G1Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.19.wasm"))
                        .instantiate(store, "G1");
    }

    public static Instance G2Instance = null;

    @Test()
    @Order(77)
    public void instantiate_G2Instance() {
        G2Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.20.wasm"))
                        .instantiate(store, "G2");
    }

    @Test()
    @Order(78)
    public void test78() {
        ExportFunction varG = G2Instance.export("g");
        var results = varG.apply();
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(79)
    public void test79() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.21.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds table access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds table access");
    }

    @Test()
    @Order(80)
    public void test80() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.22.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(81)
    public void test81() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("7"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(82)
    public void test82() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.23.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds table access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds table access");
    }

    @Test()
    @Order(83)
    public void test83() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("7")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    @Test()
    @Order(84)
    public void test84() {
        ExportFunction varCall = MtInstance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("8"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(85)
    public void test85() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.24.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds memory access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds memory access");
    }

    @Test()
    @Order(86)
    public void test86() {
        ExportFunction varCall = MtInstance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("7")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    public static Instance Mtable_exInstance = null;

    @Test()
    @Order(87)
    public void instantiate_Mtable_exInstance() {
        Mtable_exInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.25.wasm"))
                        .instantiate(store, "Mtable_ex");
    }

    public static Instance testModule14Instance = null;

    @Test()
    @Order(88)
    public void instantiate_testModule14Instance() {
        testModule14Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.26.wasm"))
                        .instantiate(store, "__ignore");
    }

    @Test()
    @Order(89)
    public void test89() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.27.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(90)
    public void test90() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.28.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance MmInstance = null;

    @Test()
    @Order(91)
    public void instantiate_MmInstance() {
        MmInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.29.wasm"))
                        .instantiate(store, "Mm");
    }

    public static Instance NmInstance = null;

    @Test()
    @Order(92)
    public void instantiate_NmInstance() {
        NmInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.30.wasm"))
                        .instantiate(store, "Nm");
    }

    @Test()
    @Order(93)
    public void test93() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    @Test()
    @Order(94)
    public void test94() {
        ExportFunction varMmLoad = NmInstance.export("Mm.load");
        var results = varMmLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    @Test()
    @Order(95)
    public void test95() {
        ExportFunction varLoad = NmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("242"), results[0].asInt());
    }

    public static Instance OmInstance = null;

    @Test()
    @Order(96)
    public void instantiate_OmInstance() {
        OmInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.31.wasm"))
                        .instantiate(store, "Om");
    }

    @Test()
    @Order(97)
    public void test97() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("167"), results[0].asInt());
    }

    @Test()
    @Order(98)
    public void test98() {
        ExportFunction varMmLoad = NmInstance.export("Mm.load");
        var results = varMmLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("167"), results[0].asInt());
    }

    @Test()
    @Order(99)
    public void test99() {
        ExportFunction varLoad = NmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("242"), results[0].asInt());
    }

    @Test()
    @Order(100)
    public void test100() {
        ExportFunction varLoad = OmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("12")));
        assertEquals(Integer.parseUnsignedInt("167"), results[0].asInt());
    }

    public static Instance testModule18Instance = null;

    @Test()
    @Order(101)
    public void instantiate_testModule18Instance() {
        testModule18Instance =
                TestModule.of(new File("target/compiled-wast/linking/spec.32.wasm"))
                        .instantiate(store, "_ignore");
    }

    @Test()
    @Order(102)
    public void test102() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.33.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds memory access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds memory access");
    }

    public static Instance PmInstance = null;

    @Test()
    @Order(103)
    public void instantiate_PmInstance() {
        PmInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.34.wasm"))
                        .instantiate(store, "Pm");
    }

    @Test()
    @Order(104)
    public void test104() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("1"), results[0].asInt());
    }

    @Test()
    @Order(105)
    public void test105() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("1"), results[0].asInt());
    }

    @Test()
    @Order(106)
    public void test106() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("3"), results[0].asInt());
    }

    @Test()
    @Order(107)
    public void test107() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("3"), results[0].asInt());
    }

    @Test()
    @Order(108)
    public void test108() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("4"), results[0].asInt());
    }

    @Test()
    @Order(109)
    public void test109() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(110)
    public void test110() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("4294967295"), results[0].asInt());
    }

    @Test()
    @Order(111)
    public void test111() {
        ExportFunction varGrow = PmInstance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("5"), results[0].asInt());
    }

    @Test()
    @Order(112)
    public void test112() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.35.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(113)
    public void test113() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    @Test()
    @Order(114)
    public void test114() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.36.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds memory access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds memory access");
    }

    @Test()
    @Order(115)
    public void test115() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("97"), results[0].asInt());
    }

    @Test()
    @Order(116)
    public void test116() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("327670")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    @Test()
    @Order(117)
    public void test117() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.37.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("out of bounds table access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds table access");
    }

    @Test()
    @Order(118)
    public void test118() {
        ExportFunction varLoad = MmInstance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("97"), results[0].asInt());
    }

    public static Instance MsInstance = null;

    @Test()
    @Order(119)
    public void instantiate_MsInstance() {
        MsInstance =
                TestModule.of(new File("target/compiled-wast/linking/spec.38.wasm"))
                        .instantiate(store, "Ms");
    }

    @Test()
    @Order(120)
    public void test120() {
        var exception =
                assertThrows(
                        UninstantiableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/linking/spec.39.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unreachable"),
                "'" + exception.getMessage() + "' doesn't contains: 'unreachable");
    }

    @Test()
    @Order(121)
    public void test121() {
        ExportFunction varGetMemory0 = MsInstance.export("get memory[0]");
        var results = varGetMemory0.apply();
        assertEquals(Integer.parseUnsignedInt("104"), results[0].asInt());
    }

    @Test()
    @Order(122)
    public void test122() {
        ExportFunction varGetTable0 = MsInstance.export("get table[0]");
        var results = varGetTable0.apply();
        assertEquals(Integer.parseUnsignedInt("57005"), results[0].asInt());
    }
}
