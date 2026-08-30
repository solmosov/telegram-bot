package io.github.solmosov.telegrambot.scanner;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ClassInstanceFactoryTest {
    @Test
    void shouldCreateInstanceWithDependencies() {
        DependencyA dependencyA = new DependencyA();
        DependencyB dependencyB = new DependencyB();
        ClassInstanceFactory factory = new ClassInstanceFactory(Map.of(DependencyA.class, dependencyA, DependencyB.class, dependencyB));
        TestClass result = (TestClass) factory.create(TestClass.class);
        assertSame(dependencyA, result.dependencyA);
        assertSame(dependencyB, result.dependencyB);
    }

    @Test
    void shouldCreateInstanceWithNoArgsConstructor() {
        ClassInstanceFactory factory = new ClassInstanceFactory(Map.of());
        NoArgsClass result = (NoArgsClass) factory.create(NoArgsClass.class);
        assertEquals("test", result.value);
    }

    @Test
    void shouldThrowExceptionWhenDependencyIsMissing() {
        ClassInstanceFactory factory = new ClassInstanceFactory(Map.of());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> factory.create(TestClass.class));
        assertEquals("java.lang.IllegalArgumentException: No dependency found for: " + DependencyA.class.getName(), exception.getCause().toString());
    }

    @Test
    void shouldWrapConstructorException() {
        ClassInstanceFactory factory = new ClassInstanceFactory(
                Map.of(
                        DependencyA.class, new DependencyA()
                )
        );

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> factory.create(FailingClass.class)
        );

        assertEquals(
                "Constructor failed",
                exception.getCause().getCause().getMessage()
        );
    }

    static class TestClass {
        private final DependencyA dependencyA;
        private final DependencyB dependencyB;

        TestClass(DependencyA dependencyA, DependencyB dependencyB) {
            this.dependencyA = dependencyA;
            this.dependencyB = dependencyB;
        }
    }

    static class NoArgsClass {
        private final String value = "test";
    }

    static class FailingClass {
        FailingClass(DependencyA dependencyA) {
            throw new RuntimeException("Constructor failed");
        }
    }

    static class DependencyA {
    }

    static class DependencyB {
    }

}