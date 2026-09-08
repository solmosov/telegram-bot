package io.github.solmosov.telegrambot.dispatcher.resolver;

import io.github.solmosov.telegrambot.exception.handler.HandlerRegistrationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CallbackParamResolverTest {

    @Nested
    class GenerateKeyTests {

        @Test
        void shouldReturnSamePattern_whenNoBrackets() {
            assertEquals(
                    "orders:page:status",
                    CallbackParamResolver.generateKey("orders:page:status")
            );
        }

        @Test
        void shouldConvertDynamicPartsToEmptyBrackets() {
            assertEquals(
                    "orders:{}:{}",
                    CallbackParamResolver.generateKey("orders:{page|int}:{sort|String}")
            );
        }

        @Test
        void shouldAcceptDynamicParametersWithoutType() {
            assertEquals(
                    "orders:{}:{}",
                    CallbackParamResolver.generateKey("orders:{page}:{sort}")
            );
        }

        @Test
        void shouldThrowException_whenStaticPartFollowsDynamicPart() {
            HandlerRegistrationException exception = assertThrows(
                    HandlerRegistrationException.class,
                    () -> CallbackParamResolver.generateKey("orders:{page}:status")
            );

            assertTrue(exception.getMessage().contains("Invalid callback pattern"));
            assertTrue(exception.getMessage().contains("orders:{page}:status"));
        }

        @Test
        void shouldThrowException_whenDynamicPartFollowsStaticPart() {
            HandlerRegistrationException exception = assertThrows(
                    HandlerRegistrationException.class,
                    () -> CallbackParamResolver.generateKey("orders:page:{status}")
            );

            assertTrue(exception.getMessage().contains("Invalid callback pattern"));
            assertTrue(exception.getMessage().contains("orders:page:{status}"));
        }
    }

    @Nested
    class ConvertToPatternKeyTests {

        @Test
        void shouldConvertAllPartsAfterFirstToDynamic() {
            assertEquals(
                    "orders:{}:{}",
                    CallbackParamResolver.convertToPatternKey("orders:2:desc")
            );
        }

        @Test
        void shouldReturnSameValue_whenOnlyOnePartExists() {
            assertEquals(
                    "orders",
                    CallbackParamResolver.convertToPatternKey("orders")
            );
        }

        @Test
        void shouldConvertMultipleParametersToDynamic() {
            assertEquals(
                    "users:{}:{}:{}",
                    CallbackParamResolver.convertToPatternKey("users:10:active:admin")
            );
        }
    }

    @Nested
    class GetParamsTests {

        @Test
        void shouldReturnEmptyMap_whenPartsCountDoesNotMatch() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:{page|int}:{sort|String}",
                    "orders:2"
            );

            assertTrue(result.isEmpty());
        }

        @Test
        void shouldResolveStringParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:{page|int}:{sort|String}",
                    "orders:2:desc"
            );

            assertEquals(2, result.get("page"));
            assertEquals("desc", result.get("sort"));
        }

        @Test
        void shouldResolveParameterWithoutType() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "users:{name}",
                    "users:john"
            );

            assertEquals("john", result.get("name"));
        }

        @Test
        void shouldResolveBooleanParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "users:{active|boolean}",
                    "users:true"
            );

            assertEquals(true, result.get("active"));
        }

        @Test
        void shouldResolveShortParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "users:{age|short}",
                    "users:25"
            );

            assertEquals((short) 25, result.get("age"));
        }

        @Test
        void shouldResolveIntParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:{page|int}",
                    "orders:5"
            );

            assertEquals(5, result.get("page"));
        }

        @Test
        void shouldResolveLongParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:{id|long}",
                    "orders:123456789"
            );

            assertEquals(123456789L, result.get("id"));
        }

        @Test
        void shouldResolveDoubleParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "products:{price|double}",
                    "products:19.99"
            );

            assertEquals(19.99, result.get("price"));
        }

        @Test
        void shouldResolveFloatParameter() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "products:{rating|float}",
                    "products:4.5"
            );

            assertEquals(4.5f, result.get("rating"));
        }

        @Test
        void shouldKeepUnknownTypeAsString() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "users:{name|unknown}",
                    "users:john"
            );

            assertEquals("john", result.get("name"));
        }

        @Test
        void shouldIgnoreStaticParts() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:page:{sort}",
                    "orders:page:desc"
            );

            assertEquals(1, result.size());
            assertEquals("desc", result.get("sort"));
        }

        @Test
        void shouldResolveMultipleParametersWithDifferentTypes() {
            Map<String, Object> result = CallbackParamResolver.getParams(
                    "orders:{page|int}:{active|boolean}:{price|double}:{rating|float}",
                    "orders:2:true:15.5:4.5"
            );

            assertEquals(4, result.size());
            assertEquals(2, result.get("page"));
            assertEquals(true, result.get("active"));
            assertEquals(15.5, result.get("price"));
            assertEquals(4.5f, result.get("rating"));
        }
    }
}