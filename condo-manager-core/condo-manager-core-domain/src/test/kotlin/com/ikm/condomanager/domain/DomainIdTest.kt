package com.ikm.condomanager.domain

import jakarta.validation.Validation
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

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @ParameterizedTest
    @MethodSource("validDomainIdParams")
    fun `should be a valid DomainId`(id: String, version: Long) {
        // given
        val domainId = DomainId(id, version)
        // when
        val validations = validator.validate(domainId)
        // then
        assertEquals(0, validations.size)
        assertEquals(version, domainId.version)
        assertEquals(id, domainId.id)
    }

    @ParameterizedTest
    @MethodSource("invalidDomainIdParams")
    fun `should be a not valid DomainId`(domainId: DomainId, message: String) {
        // when
        val validations = validator.validate(domainId)
        // then
        assertTrue(
            validations.map { it.messageTemplate }.contains(message),
            "Actual validations: $validations"
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
                Arguments.of(DomainId("", 100), "{jakarta.validation.constraints.NotBlank.message}"),
                Arguments.of(DomainId(" ", 100), "{jakarta.validation.constraints.NotBlank.message}"),
                Arguments.of(
                    DomainId(UUID.randomUUID().toString(), -1),
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                ),
                Arguments.of(
                    DomainId(UUID.randomUUID().toString(), -1000),
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                ),
                Arguments.of(
                    DomainId(UUID.randomUUID().toString(), Long.MIN_VALUE),
                    "{jakarta.validation.constraints.PositiveOrZero.message}"
                )
            )
    }
}
