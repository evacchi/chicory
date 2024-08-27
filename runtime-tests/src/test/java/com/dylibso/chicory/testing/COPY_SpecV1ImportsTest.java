package com.dylibso.chicory.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.dylibso.chicory.imports.SpecV1ImportsHostFuncs;
import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.exceptions.ChicoryException;
import com.dylibso.chicory.wasm.exceptions.InvalidException;
import com.dylibso.chicory.wasm.exceptions.MalformedException;
import com.dylibso.chicory.wasm.exceptions.UnlinkableException;
import com.dylibso.chicory.wasm.types.Value;
import java.io.File;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class COPY_SpecV1ImportsTest {

    public static Store store = new Store()
            .addHostImports(Spectest.toHostImports());


    public static Instance testModule0Instance = null;

    @Test()
    @Order(0)
    public void instantiate_testModule0Instance() {
        testModule0Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.0.wasm"))
                        .instantiate(store, "test");
    }

    public static Instance testModule1Instance = null;

    @Test()
    @Order(1)
    public void instantiate_testModule1Instance() {
        testModule1Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.1.wasm"))
                        .instantiate(store, "testModule1");
    }

    @Test()
    @Order(2)
    public void test2() {
        ExportFunction varPrint32 = testModule1Instance.export("print32");
        var results = varPrint32.apply(Value.i32(Integer.parseUnsignedInt("13")));
    }

    @Test()
    @Order(3)
    public void test3() {
        ExportFunction varPrint64 = testModule1Instance.export("print64");
        var results = varPrint64.apply(Value.i64(Long.parseUnsignedLong("24")));
    }

    @Test()
    @Order(4)
    public void test4() {
        var exception =
                assertThrows(
                        InvalidException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.2.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown type"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown type");
    }

    public static Instance testModule2Instance = null;

    @Test()
    @Order(5)
    public void instantiate_testModule2Instance() {
        testModule2Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.3.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(6)
    public void test6() {
        ExportFunction varPrintI32 = testModule2Instance.export("print_i32");
        var results = varPrintI32.apply(Value.i32(Integer.parseUnsignedInt("13")));
    }

    public static Instance testModule3Instance = null;

    @Test()
    @Order(7)
    public void instantiate_testModule3Instance() {
        testModule3Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.4.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(8)
    public void test8() {
        ExportFunction varPrintI32 = testModule3Instance.export("print_i32");
        var results =
                varPrintI32.apply(
                        Value.i32(Integer.parseUnsignedInt("5")),
                        Value.i32(Integer.parseUnsignedInt("11")));
        assertEquals(Integer.parseUnsignedInt("16"), results[0].asInt());
    }

    public static Instance testModule4Instance = null;

    @Test()
    @Order(9)
    public void instantiate_testModule4Instance() {
        testModule4Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.5.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule5Instance = null;

    @Test()
    @Order(10)
    public void instantiate_testModule5Instance() {
        testModule5Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.6.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule6Instance = null;

    @Test()
    @Order(11)
    public void instantiate_testModule6Instance() {
        testModule6Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.7.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule7Instance = null;

    @Test()
    @Order(12)
    public void instantiate_testModule7Instance() {
        testModule7Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.8.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule8Instance = null;

    @Test()
    @Order(13)
    public void instantiate_testModule8Instance() {
        testModule8Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.9.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule9Instance = null;

    @Test()
    @Order(14)
    public void instantiate_testModule9Instance() {
        testModule9Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.10.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule10Instance = null;

    @Test()
    @Order(15)
    public void instantiate_testModule10Instance() {
        testModule10Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.11.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(16)
    public void test16() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.12.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(17)
    public void test17() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.13.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(18)
    public void test18() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.14.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(19)
    public void test19() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.15.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(20)
    public void test20() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.16.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(21)
    public void test21() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.17.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(22)
    public void test22() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.18.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(23)
    public void test23() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.19.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(24)
    public void test24() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.20.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(25)
    public void test25() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.21.wasm"))
                                        .instantiate(store, "__failure"));

        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(26)
    public void test26() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.22.wasm"))
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
                                TestModule.of(new File("target/compiled-wast/imports/spec.23.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(28)
    public void test28() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.24.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(29)
    public void test29() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.25.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(30)
    public void test30() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.26.wasm"))
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
                                TestModule.of(new File("target/compiled-wast/imports/spec.27.wasm"))
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
                                TestModule.of(new File("target/compiled-wast/imports/spec.28.wasm"))
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
                                TestModule.of(new File("target/compiled-wast/imports/spec.29.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(34)
    public void test34() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.30.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(35)
    public void test35() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.31.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(36)
    public void test36() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.32.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(37)
    public void test37() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.33.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(38)
    public void test38() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.34.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(39)
    public void test39() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.35.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance testModule11Instance = null;

    @Test()
    @Order(40) // FIXME
    public void instantiate_testModule11Instance() {
        testModule11Instance = TestModule.of(
            new File("target/compiled-wast/imports/spec.36.wasm"))
          .instantiate(store, "testModule11");
    }

    @Test()
    @Order(41)
    public void test41() {
        ExportFunction varGet0 = testModule11Instance.export("get-0");
        var results = varGet0.apply();
        assertEquals(Integer.parseUnsignedInt("666"), results[0].asInt());
    }

    @Test()
    @Order(42)
    public void test42() {
        ExportFunction varGet1 = testModule11Instance.export("get-1");
        var results = varGet1.apply();
        assertEquals(Integer.parseUnsignedInt("666"), results[0].asInt());
    }

    @Test()
    @Order(43)
    public void test43() {
        ExportFunction varGetX = testModule11Instance.export("get-x");
        var results = varGetX.apply();
        assertEquals(Integer.parseUnsignedInt("666"), results[0].asInt());
    }

    @Test()
    @Order(44)
    public void test44() {
        ExportFunction varGetY = testModule11Instance.export("get-y");
        var results = varGetY.apply();
        assertEquals(Integer.parseUnsignedInt("666"), results[0].asInt());
    }

    public static Instance testModule12Instance = null;

    @Test()
    @Order(45)
    public void instantiate_testModule12Instance() {
        testModule12Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.37.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule13Instance = null;

    @Test()
    @Order(46)
    public void instantiate_testModule13Instance() {
        testModule13Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.38.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule14Instance = null;

    @Test()
    @Order(47)
    public void instantiate_testModule14Instance() {
        testModule14Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.39.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(48)
    public void test48() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.40.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(49)
    public void test49() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.41.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(50)
    public void test50() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.42.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(51)
    public void test51() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.43.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(52)
    public void test52() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.44.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(53)
    public void test53() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.45.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(54)
    public void test54() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.46.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(55)
    public void test55() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.47.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(56)
    public void test56() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.48.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(57)
    public void test57() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.49.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(58)
    public void test58() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.50.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(59)
    public void test59() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.51.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(60)
    public void test60() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.52.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(61)
    public void test61() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.53.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(62)
    public void test62() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.54.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(63)
    public void test63() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.55.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(64)
    public void test64() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.56.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(65)
    public void test65() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.57.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(66)
    public void test66() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.58.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(67)
    public void test67() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.59.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance testModule15Instance = null;

    @Test()
    @Order(68)
    public void instantiate_testModule15Instance() {
        testModule15Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.60.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(69)
    public void test69() {
        ExportFunction varCall = testModule15Instance.export("call");
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
        ExportFunction varCall = testModule15Instance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("11"), results[0].asInt());
    }

    @Test()
    @Order(71)
    public void test71() {
        ExportFunction varCall = testModule15Instance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("22"), results[0].asInt());
    }

    @Test()
    @Order(72)
    public void test72() {
        ExportFunction varCall = testModule15Instance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("3"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(73)
    public void test73() {
        ExportFunction varCall = testModule15Instance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("100"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    public static Instance testModule16Instance = null;

    @Test()
    @Order(74)
    public void instantiate_testModule16Instance() {
        testModule16Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.61.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(75)
    public void test75() {
        ExportFunction varCall = testModule16Instance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("0"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(76)
    public void test76() {
        ExportFunction varCall = testModule16Instance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("11"), results[0].asInt());
    }

    @Test()
    @Order(77)
    public void test77() {
        ExportFunction varCall = testModule16Instance.export("call");
        var results = varCall.apply(Value.i32(Integer.parseUnsignedInt("2")));
        assertEquals(Integer.parseUnsignedInt("22"), results[0].asInt());
    }

    @Test()
    @Order(78)
    public void test78() {
        ExportFunction varCall = testModule16Instance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("3"))));
        assertTrue(
                exception.getMessage().contains("uninitialized element"),
                "'" + exception.getMessage() + "' doesn't contains: 'uninitialized element");
    }

    @Test()
    @Order(79)
    public void test79() {
        ExportFunction varCall = testModule16Instance.export("call");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varCall.apply(Value.i32(Integer.parseUnsignedInt("100"))));
        assertTrue(
                exception.getMessage().contains("undefined element"),
                "'" + exception.getMessage() + "' doesn't contains: 'undefined element");
    }

    public static Instance testModule17Instance = null;

    @Test()
    @Order(80)
    public void instantiate_testModule17Instance() {
        testModule17Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.62.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule18Instance = null;

    @Test()
    @Order(81)
    public void instantiate_testModule18Instance() {
        testModule18Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.63.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule19Instance = null;

    @Test()
    @Order(82)
    public void instantiate_testModule19Instance() {
        testModule19Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.64.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule20Instance = null;

    @Test()
    @Order(83)
    public void instantiate_testModule20Instance() {
        testModule20Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.65.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule21Instance = null;

    @Test()
    @Order(84)
    public void instantiate_testModule21Instance() {
        testModule21Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.66.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule22Instance = null;

    @Test()
    @Order(85)
    public void instantiate_testModule22Instance() {
        testModule22Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.67.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule23Instance = null;

    @Test()
    @Order(86)
    public void instantiate_testModule23Instance() {
        testModule23Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.68.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule24Instance = null;

    @Test()
    @Order(87)
    public void instantiate_testModule24Instance() {
        testModule24Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.69.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule25Instance = null;

    @Test()
    @Order(88)
    public void instantiate_testModule25Instance() {
        testModule25Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.70.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule26Instance = null;

    @Test()
    @Order(89)
    public void instantiate_testModule26Instance() {
        testModule26Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.71.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule27Instance = null;

    @Test()
    @Order(90)
    public void instantiate_testModule27Instance() {
        testModule27Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.72.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule28Instance = null;

    @Test()
    @Order(91)
    public void instantiate_testModule28Instance() {
        testModule28Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.73.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule29Instance = null;

    @Test()
    @Order(92)
    public void instantiate_testModule29Instance() {
        testModule29Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.74.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule30Instance = null;

    @Test()
    @Order(93)
    public void instantiate_testModule30Instance() {
        testModule30Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.75.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule31Instance = null;

    @Test()
    @Order(94)
    public void instantiate_testModule31Instance() {
        testModule31Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.76.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule32Instance = null;

    @Test()
    @Order(95)
    public void instantiate_testModule32Instance() {
        testModule32Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.77.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule33Instance = null;

    @Test()
    @Order(96)
    public void instantiate_testModule33Instance() {
        testModule33Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.78.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule34Instance = null;

    @Test()
    @Order(97)
    public void instantiate_testModule34Instance() {
        testModule34Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.79.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule35Instance = null;

    @Test()
    @Order(98)
    public void instantiate_testModule35Instance() {
        testModule35Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.80.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule36Instance = null;

    @Test()
    @Order(99)
    public void instantiate_testModule36Instance() {
        testModule36Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.81.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule37Instance = null;

    @Test()
    @Order(100)
    public void instantiate_testModule37Instance() {
        testModule37Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.82.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(101)
    public void test101() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.83.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(102)
    public void test102() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.84.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(103)
    public void test103() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.85.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(104)
    public void test104() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.86.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(105)
    public void test105() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.87.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(106)
    public void test106() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.88.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(107)
    public void test107() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.89.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(108)
    public void test108() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.90.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(109)
    public void test109() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.91.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(110)
    public void test110() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.92.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(111)
    public void test111() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.93.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(112)
    public void test112() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.94.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance testModule38Instance = null;

    @Test()
    @Order(113)
    public void instantiate_testModule38Instance() {
        testModule38Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.95.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(114)
    public void test114() {
        ExportFunction varLoad = testModule38Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    @Test()
    @Order(115)
    public void test115() {
        ExportFunction varLoad = testModule38Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("10")));
        assertEquals(Integer.parseUnsignedInt("16"), results[0].asInt());
    }

    @Test()
    @Order(116)
    public void test116() {
        ExportFunction varLoad = testModule38Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("8")));
        assertEquals(Integer.parseUnsignedInt("1048576"), results[0].asInt());
    }

    @Test()
    @Order(117)
    public void test117() {
        ExportFunction varLoad = testModule38Instance.export("load");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varLoad.apply(Value.i32(Integer.parseUnsignedInt("1000000"))));
        assertTrue(
                exception.getMessage().contains("out of bounds memory access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds memory access");
    }

    public static Instance testModule39Instance = null;

    @Test()
    @Order(118)
    public void instantiate_testModule39Instance() {
        testModule39Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.96.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(119)
    public void test119() {
        ExportFunction varLoad = testModule39Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("0"), results[0].asInt());
    }

    @Test()
    @Order(120)
    public void test120() {
        ExportFunction varLoad = testModule39Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("10")));
        assertEquals(Integer.parseUnsignedInt("16"), results[0].asInt());
    }

    @Test()
    @Order(121)
    public void test121() {
        ExportFunction varLoad = testModule39Instance.export("load");
        var results = varLoad.apply(Value.i32(Integer.parseUnsignedInt("8")));
        assertEquals(Integer.parseUnsignedInt("1048576"), results[0].asInt());
    }

    @Test()
    @Order(122)
    public void test122() {
        ExportFunction varLoad = testModule39Instance.export("load");
        var exception =
                assertThrows(
                        ChicoryException.class,
                        () -> varLoad.apply(Value.i32(Integer.parseUnsignedInt("1000000"))));
        assertTrue(
                exception.getMessage().contains("out of bounds memory access"),
                "'" + exception.getMessage() + "' doesn't contains: 'out of bounds memory access");
    }

    @Test()
    @Order(123)
    public void test123() {
        var exception =
                assertThrows(
                        InvalidException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.97.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("multiple memories"),
                "'" + exception.getMessage() + "' doesn't contains: 'multiple memories");
    }

    @Test()
    @Order(124)
    public void test124() {
        var exception =
                assertThrows(
                        InvalidException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.98.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("multiple memories"),
                "'" + exception.getMessage() + "' doesn't contains: 'multiple memories");
    }

    @Test()
    @Order(125)
    public void test125() {
        var exception =
                assertThrows(
                        InvalidException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.99.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("multiple memories"),
                "'" + exception.getMessage() + "' doesn't contains: 'multiple memories");
    }

    public static Instance testModule40Instance = null;

    @Test()
    @Order(126)
    public void instantiate_testModule40Instance() {
        testModule40Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.100.wasm"))
                        .instantiate(store, "testModule40");
    }

    public static Instance testModule41Instance = null;

    @Test()
    @Order(127)
    public void instantiate_testModule41Instance() {
        testModule41Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.101.wasm"))
                        .instantiate(store, "testModule41");
    }

    public static Instance testModule42Instance = null;

    @Test()
    @Order(128)
    public void instantiate_testModule42Instance() {
        testModule42Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.102.wasm"))
                        .instantiate(store, "testModule42");
    }

    public static Instance testModule43Instance = null;

    @Test()
    @Order(129)
    public void instantiate_testModule43Instance() {
        testModule43Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.103.wasm"))
                        .instantiate(store, "testModule43");
    }

    public static Instance testModule44Instance = null;

    @Test()
    @Order(130)
    public void instantiate_testModule44Instance() {
        testModule44Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.104.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule45Instance = null;

    @Test()
    @Order(131)
    public void instantiate_testModule45Instance() {
        testModule45Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.105.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule46Instance = null;

    @Test()
    @Order(132)
    public void instantiate_testModule46Instance() {
        testModule46Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.106.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule47Instance = null;

    @Test()
    @Order(133)
    public void instantiate_testModule47Instance() {
        testModule47Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.107.wasm"))
                        .instantiate(store, "testModule");
    }

    public static Instance testModule48Instance = null;

    @Test()
    @Order(134)
    public void instantiate_testModule48Instance() {
        testModule48Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.108.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(135)
    public void test135() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.109.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(136)
    public void test136() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.110.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }

    @Test()
    @Order(137)
    public void test137() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.111.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(138) // FIXME
    public void test138() {
        var exception = assertThrows(UnlinkableException.class, () -> TestModule.of(
            new File("target/compiled-wast/imports/spec.112.wasm"))
          .withHostImports(SpecV1ImportsHostFuncs.fallback())
          .build());
        assertTrue(exception.getMessage().contains("incompatible import type"), "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(139)
    public void test139() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.113.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(140)
    public void test140() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.114.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(141)
    public void test141() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.115.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(142)
    public void test142() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.116.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(143)
    public void test143() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.117.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(144)
    public void test144() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.118.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(145)
    public void test145() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.119.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(146)
    public void test146() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.120.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(147)
    public void test147() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.121.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    @Test()
    @Order(148)
    public void test148() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.122.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("incompatible import type"),
                "'" + exception.getMessage() + "' doesn't contains: 'incompatible import type");
    }

    public static Instance testModule49Instance = null;

    @Test()
    @Order(149)
    public void instantiate_testModule49Instance() {
        testModule49Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.123.wasm"))
                        .instantiate(store, "testModule49");
    }

    @Test()
    @Order(150)
    public void test150() {
        ExportFunction varGrow = testModule49Instance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("1"), results[0].asInt());
    }

    @Test()
    @Order(151)
    public void test151() {
        ExportFunction varGrow = testModule49Instance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("1"), results[0].asInt());
    }

    @Test()
    @Order(152)
    public void test152() {
        ExportFunction varGrow = testModule49Instance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    @Test()
    @Order(153)
    public void test153() {
        ExportFunction varGrow = testModule49Instance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("1")));
        assertEquals(Integer.parseUnsignedInt("4294967295"), results[0].asInt());
    }

    @Test()
    @Order(154)
    public void test154() {
        ExportFunction varGrow = testModule49Instance.export("grow");
        var results = varGrow.apply(Value.i32(Integer.parseUnsignedInt("0")));
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    public static Instance MgmInstance = null;

    @Test()
    @Order(155)
    public void instantiate_MgmInstance() {
        MgmInstance =
                TestModule.of(new File("target/compiled-wast/imports/spec.124.wasm"))
                        .instantiate(store, "grown-memory");
    }

    @Test()
    @Order(156)
    public void test156() {
        ExportFunction varGrow = MgmInstance.export("grow");
        var results = varGrow.apply();
        assertEquals(Integer.parseUnsignedInt("1"), results[0].asInt());
    }

    public static Instance Mgim1Instance = null;

    @Test()
    @Order(157)
    public void instantiate_Mgim1Instance() {
        Mgim1Instance = TestModule.of(
            new File("target/compiled-wast/imports/spec.125.wasm"))
          .instantiate(store, "grown-imported-memory");
    }

    @Test()
    @Order(158)
    public void test158() {
        ExportFunction varGrow = Mgim1Instance.export("grow");
        var results = varGrow.apply();
        assertEquals(Integer.parseUnsignedInt("2"), results[0].asInt());
    }

    public static Instance Mgim2Instance = null;

    @Test()
    @Order(159)
    public void instantiate_Mgim2Instance() {
        Mgim2Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.126.wasm"))
                        .instantiate(store, "Mgim2");
    }

    @Test()
    @Order(160)
    public void test160() {
        ExportFunction varSize = Mgim2Instance.export("size");
        var results = varSize.apply();
        assertEquals(Integer.parseUnsignedInt("3"), results[0].asInt());
    }

    @Test()
    @Order(161)
    public void test161() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.127.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after function"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after function");
    }

    @Test()
    @Order(162)
    public void test162() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.128.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after function"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after function");
    }

    @Test()
    @Order(163)
    public void test163() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.129.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after function"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after function");
    }

    @Test()
    @Order(164)
    public void test164() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.130.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after function"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after function");
    }

    @Test()
    @Order(165)
    public void test165() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.131.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after global"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after global");
    }

    @Test()
    @Order(166)
    public void test166() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.132.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after global"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after global");
    }

    @Test()
    @Order(167)
    public void test167() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.133.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after global"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after global");
    }

    @Test()
    @Order(168)
    public void test168() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.134.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after global"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after global");
    }

    @Test()
    @Order(169)
    public void test169() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.135.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after table"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after table");
    }

    @Test()
    @Order(170)
    public void test170() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.136.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after table"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after table");
    }

    @Test()
    @Order(171)
    public void test171() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.137.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after table"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after table");
    }

    @Test()
    @Order(172)
    public void test172() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.138.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after table"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after table");
    }

    @Test()
    @Order(173)
    public void test173() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.139.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after memory"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after memory");
    }

    @Test()
    @Order(174)
    public void test174() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.140.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after memory"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after memory");
    }

    @Test()
    @Order(175)
    public void test175() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.141.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after memory"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after memory");
    }

    @Test()
    @Order(176)
    public void test176() {
        var exception =
                assertThrows(
                        MalformedException.class,
                        () ->
                                TestModule.of(new File("target/compiled-wast/imports/spec.142.wat"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("import after memory"),
                "'" + exception.getMessage() + "' doesn't contains: 'import after memory");
    }

    public static Instance testModule53Instance = null;

    @Test()
    @Order(177)
    public void instantiate_testModule53Instance() {
        testModule53Instance =
                TestModule.of(new File("target/compiled-wast/imports/spec.143.wasm"))
                        .instantiate(store, "testModule");
    }

    @Test()
    @Order(178)
    public void test178() {
        var exception =
                assertThrows(
                        UnlinkableException.class,
                        () ->
                                TestModule.of(
                                                new File(
                                                        "target/compiled-wast/imports/spec.144.wasm"))
                                        .instantiate(store, "__failure"));
        assertTrue(
                exception.getMessage().contains("unknown import"),
                "'" + exception.getMessage() + "' doesn't contains: 'unknown import");
    }
}
