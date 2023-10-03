package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.CondominiumAddressEntity
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.GeoLocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal

/**
 * Unit test for [CondominiumAddress] related conversions.
 */
class CondominiumAddressConverterTest {

    @Test
    fun `should convert CondominiumAddress to CondominiumAddressEntity`() {
        // given
        val condominiumAddress = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = GeoLocation(BigDecimal.ONE, BigDecimal.ZERO)
        )
        // when
        val condominiumAddressEntity = condominiumAddress.convertToCondominiumAddressEntity()
        // then
        with(condominiumAddressEntity) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertEquals(BigDecimal.ONE, lat)
            assertEquals(BigDecimal.ZERO, lon)
        }
    }

    @Test
    fun `should merge CondominiumAddress to CondominiumAddressEntity`() {
        // given
        val condominiumAddress = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = GeoLocation(BigDecimal.ONE, BigDecimal.ZERO)
        )
        val condominiumAddressEntity = CondominiumAddressEntity(
            city = "",
            street = "",
            houseNumber = 0
        )

        // when
        val merged = condominiumAddress.mergeToCondominiumAddressEntity(condominiumAddressEntity)
        // then
        assertSame(condominiumAddressEntity, merged)
        with(merged) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertEquals(BigDecimal.ONE, lat)
            assertEquals(BigDecimal.ZERO, lon)
        }
    }

    @Test
    fun `should convert CondominiumAddressEntity to CondominiumAddress`() {
        // given
        val condominiumAddressEntity = CondominiumAddressEntity(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            lat = BigDecimal.ONE,
            lon = BigDecimal.ZERO
        )
        // when
        val condominiumAddress = condominiumAddressEntity.convertToCondominiumAddress()
        // then
        with(condominiumAddress) {
            assertEquals("City Name", city)
            assertEquals("Street Name", street)
            assertEquals(1, houseNumber)
            assertNotNull(location)
            assertEquals(location!!.lat, BigDecimal.ONE)
            assertEquals(location!!.lon, BigDecimal.ZERO)
        }
    }

    @ParameterizedTest
    @CsvSource(",", "1,", ",1")
    fun `given CondominiumAddressEntity with null lon or lon, should convert to CondominiumAddress with null location`(
        latStr: String?,
        lonStr: String?
    ) {
        // given
        val condominiumAddressEntity = CondominiumAddressEntity(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            lat = latStr?.let { BigDecimal(it) },
            lon = lonStr?.let { BigDecimal(it) }
        )
        // when
        val condominiumAddress = condominiumAddressEntity.convertToCondominiumAddress()
        // then
        assertNull(condominiumAddress.location)
    }
}
