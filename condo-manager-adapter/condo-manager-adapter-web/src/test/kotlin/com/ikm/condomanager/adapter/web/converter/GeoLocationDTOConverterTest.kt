package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.GeoLocationDTO
import com.ikm.condomanager.domain.GeoLocation
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigDecimal
import kotlin.test.Test

/**
 * Unit test for [GeoLocationDTO] related conversions.
 */
class GeoLocationDTOConverterTest {
    @Test
    fun `should convert GeoLocationDTO to GeoLocation`() {
        // given
        val geoLocationDTO = GeoLocationDTO(
            lat = BigDecimal.ZERO,
            lon = BigDecimal.ONE
        )
        // when
        val geoLocation = geoLocationDTO.convertToGeoLocation()
        // then
        with(geoLocation) {
            assertEquals(BigDecimal.ZERO, lat)
            assertEquals(BigDecimal.ONE, lon)
        }
    }

    @Test
    fun `should convert GeoLocation to GeoLocationDTO`() {
        // given
        val geoLocation = GeoLocation(
            lat = BigDecimal.ZERO,
            lon = BigDecimal.ONE
        )
        // when
        val geoLocationDTO = geoLocation.convertToGeoLocationDTO()
        // then
        with(geoLocationDTO) {
            assertEquals(BigDecimal.ZERO, lat)
            assertEquals(BigDecimal.ONE, lon)
        }
    }
}
