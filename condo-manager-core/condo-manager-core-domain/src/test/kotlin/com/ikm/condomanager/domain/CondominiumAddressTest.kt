package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Unit test for [CondominiumAddress].
 */
class CondominiumAddressTest {
    @Test
    fun `should create CondominiumAddress`() {
        // when
        val address = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1
        )
        // then
        assertEquals("City Name", address.city)
        assertEquals("Street Name", address.street)
        assertEquals(1, address.houseNumber)
        assertNull(address.location)
    }

    @ParameterizedTest
    @MethodSource("validParams")
    fun `should be a valid CondominiumAddress`(parameters: CreateParameters) {
        // when
        val address = CondominiumAddress(
            city = parameters.city,
            street = parameters.street,
            houseNumber = parameters.houseNumber,
            location = parameters.location
        )
        // then
        assertNotNull(address)
    }

    @ParameterizedTest
    @MethodSource("invalidParams")
    fun `should be a not valid CondominiumAddress`(
        parameters: CreateParameters,
        propertyToMessage: Pair<String, String>
    ) {
        // when
        val exception = assertThrows(ConstraintViolationException::class.java) {
            CondominiumAddress(
                city = parameters.city,
                street = parameters.street,
                houseNumber = parameters.houseNumber,
                location = parameters.location
            )
        }
        // then
        exception.assert(propertyToMessage)
    }

    companion object {
        @JvmStatic
        fun validParams() =
            listOf(
                Arguments.of(
                    CreateParameters(
                        city = randomAlphabetic(50),
                        street = "Street Name",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = randomAlphabetic(50),
                        street = "Street Name",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = randomAlphabetic(1),
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = randomAlphabetic(200),
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "Street Name",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "Street Name",
                        houseNumber = Short.MAX_VALUE,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    )
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "Street Name",
                        houseNumber = 1,
                        location = null
                    )
                )
            )

        @JvmStatic
        fun invalidParams() =
            listOf(
                Arguments.of(
                    CreateParameters(
                        city = "",
                        street = "Street Name",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "city" to "{jakarta.validation.constraints.NotBlank.message}"
                ),
                Arguments.of(
                    CreateParameters(
                        city = randomAlphabetic(51),
                        street = "Street Name",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "city" to "{jakarta.validation.constraints.Size.message}"
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "",
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "street" to "{jakarta.validation.constraints.NotBlank.message}"
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = randomAlphabetic(201),
                        houseNumber = 1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "street" to "{jakarta.validation.constraints.Size.message}"
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "Street Name",
                        houseNumber = 0,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "houseNumber" to "{jakarta.validation.constraints.Positive.message}"
                ),
                Arguments.of(
                    CreateParameters(
                        city = "City Name",
                        street = "Street Name",
                        houseNumber = -1,
                        location = GeoLocation(BigDecimal.ONE, BigDecimal.ONE)
                    ),
                    "houseNumber" to "{jakarta.validation.constraints.Positive.message}"
                )
            )
    }

    data class CreateParameters(
        val city: String,
        val street: String,
        val houseNumber: Short,
        val location: GeoLocation?
    )
}
