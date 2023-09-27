package com.ikm.condomanager.domain

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Unit test for [Condominium].
 */
class CondominiumTest {
    @Test
    fun `should create Condominium`() {
        // given
        val address = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1
        )
        // when
        val condominium = Condominium(address)
        // then
        assertEquals(condominium.address, address)
        assertNull(condominium.id)
        assertEquals("Condominium(address=$address, id=null)", condominium.toString())
    }

    @Test
    fun `should modify Condominium`() {
        // given
        val address = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1
        )
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        val condominium = Condominium(address, id)
        // when
        val newAddress = condominium.address.copy(houseNumber = 10)
        condominium.address = newAddress
        condominium.validate()
        // then
        assertEquals(condominium.address.houseNumber, 10)
        assertEquals(id, condominium.id)
        assertEquals("Condominium(address=$newAddress, id=$id)", condominium.toString())
    }
}
