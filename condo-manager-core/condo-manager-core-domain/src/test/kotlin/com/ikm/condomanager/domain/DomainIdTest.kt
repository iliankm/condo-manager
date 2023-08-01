package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit test for [DomainId].
 */
class DomainIdTest {

    @ParameterizedTest
    @MethodSource("validDomainIdParams")
    fun `should be a valid DomainId`(id: String, version: Long) {
        // given & when
        val domainId = DomainId(id, version)
        // then
        assertEquals(version, domainId.version)
        assertEquals(id, domainId.id)
    }

    @ParameterizedTest
    @MethodSource("invalidDomainIdParams")
    fun `should be a not valid DomainId`(id: String, version: Long, message: String) {
        // given & when
        val exception = assertThrows(ConstraintViolationException::class.java) {
            DomainId(id, version)
        }
        // then
        assertTrue(
            exception.constraintViolations.map { it.messageTemplate }.contains(message),
            "Actual validations: ${exception.constraintViolations}"
        )
    }

    companion object {
        @JvmStatic
        fun validDomainIdParams() =
            listOf(
                Arguments.of(UUID.randomUUID().toString(), 0),
                Arguments.of(UUID.randomUUID().toString(), 1),
                Arguments.of(UUID.randomUUID().toString(), Long.MAX_VALUE)
            )

        @JvmStatic
        fun invalidDomainIdParams() =
            listOf(
                Arguments.of("", 100, "{jakarta.validation.constraints.NotBlank.message}"),
                Arguments.of(" ", 100, "{jakarta.validation.constraints.NotBlank.message}"),
                Arguments.of(
                    UUID.randomUUID().toString(),
                    -1,
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                ),
                Arguments.of(
                    UUID.randomUUID().toString(),
                    -1000,
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                ),
                Arguments.of(
                    UUID.randomUUID().toString(),
                    Long.MIN_VALUE,
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                )
            )
    }
}
