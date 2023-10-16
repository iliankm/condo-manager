package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.GeoLocationDTO
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.GeoLocation
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

/**
 * Unit test for [CondominiumAddressDTO] related conversions.
 */
class CondominiumAddressDTOConverterTest {
    @Test
    fun `should convert CondominiumAddressDTO to CondominiumAddress`() {
        // given
        mockkStatic(GeoLocationDTO::convertToGeoLocation)
        val locationDTO = mockk<GeoLocationDTO>()
        val condominiumAddressDTO = CondominiumAddressDTO(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = locationDTO
        )
        val location = mockk<GeoLocation>()
        every { locationDTO.convertToGeoLocation() } returns location
        // when
        val condominiumAddress = condominiumAddressDTO.convertToCondominiumAddress()
        // then
        with(condominiumAddress) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertSame(location, location)
        }
        verifyAll {
            locationDTO.convertToGeoLocation()
        }
    }

    @Test
    fun `given CondominiumAddressDTO with location null, should convert to CondominiumAddress`() {
        // given
        val condominiumAddressDTO = CondominiumAddressDTO(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = null
        )
        // when
        val condominiumAddress = condominiumAddressDTO.convertToCondominiumAddress()
        // then
        with(condominiumAddress) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertNull(location)
        }
    }

    @Test
    fun `should convert CondominiumAddress to CondominiumAddressDTO`() {
        // given
        mockkStatic(GeoLocation::convertToGeoLocationDTO)
        val location = mockk<GeoLocation>()
        val condominiumAddress = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = location
        )
        val geoLocationDTO = mockk<GeoLocationDTO>()
        every { location.convertToGeoLocationDTO() } returns geoLocationDTO
        // when
        val condominiumAddressDTO = condominiumAddress.convertToCondominiumAddressDTO()
        // then
        with(condominiumAddressDTO) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertSame(geoLocationDTO, this.location)
        }
    }

    @Test
    fun `given CondominiumAddress with location null, should convert to CondominiumAddressDTO`() {
        // given
        val condominiumAddress = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = null
        )
        // when
        val condominiumAddressDTO = condominiumAddress.convertToCondominiumAddressDTO()
        // then
        with(condominiumAddressDTO) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertNull(this.location)
        }
    }
}
