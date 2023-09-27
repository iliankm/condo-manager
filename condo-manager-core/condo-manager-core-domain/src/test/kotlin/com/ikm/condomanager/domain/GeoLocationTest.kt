package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import kotlin.test.Test

/**
 * Unit test for [GeoLocation].
 */
class GeoLocationTest {
    @Test
    fun `should create GeoLocation`() {
        // given
        val lat = "42.6413"
        val lon = "24.8059"
        // when
        val location = GeoLocation(
            lat = BigDecimal(lat),
            lon = BigDecimal(lon)
        )
        // then
        assertEquals(lat, location.lat.toString())
        assertEquals(lon, location.lon.toString())
    }

    @ParameterizedTest
    @MethodSource("validParams")
    fun `should be a valid GeoLocation`(lat: BigDecimal, lon: BigDecimal) {
        // given
        val location = GeoLocation(lat, lon)
        // then
        assertNotNull(location)
    }

    @ParameterizedTest
    @MethodSource("invalidParams")
    fun `should be a not valid GeoLocation`(lat: BigDecimal, lon: BigDecimal, propertyToMessage: Pair<String, String>) {
        // when
        val exception = assertThrows(ConstraintViolationException::class.java) {
            GeoLocation(lat, lon)
        }
        // then
        exception.assert(propertyToMessage)
    }

    companion object {
        @JvmStatic
        fun validParams() =
            listOf(
                Arguments.of(BigDecimal("42.6413"), BigDecimal("24.8059")),
                Arguments.of(BigDecimal("-90"), BigDecimal.ZERO),
                Arguments.of(BigDecimal("90"), BigDecimal.ZERO),
                Arguments.of(BigDecimal.ZERO, BigDecimal("-180")),
                Arguments.of(BigDecimal.ZERO, BigDecimal("180"))
            )

        @JvmStatic
        fun invalidParams() =
            listOf(
                Arguments.of(
                    BigDecimal("-90.0001"),
                    BigDecimal.ZERO,
                    "lat" to "{jakarta.validation.constraints.DecimalMin.message}"
                ),
                Arguments.of(
                    BigDecimal("90.0001"),
                    BigDecimal.ZERO,
                    "lat" to "{jakarta.validation.constraints.DecimalMax.message}"
                ),
                Arguments.of(
                    BigDecimal.ZERO,
                    BigDecimal("-180.0001"),
                    "lon" to "{jakarta.validation.constraints.DecimalMin.message}"
                ),
                Arguments.of(
                    BigDecimal.ZERO,
                    BigDecimal("180.0001"),
                    "lon" to "{jakarta.validation.constraints.DecimalMax.message}"
                )
            )
    }
}
