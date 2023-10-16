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
            houseNumber = 1,
            location = null
        )
        // when
        val condominium = Condominium.create(address)
        // then
        assertNull(condominium.id)
        assertEquals(condominium.address, address)
        assertEquals("Condominium(id=null, address=$address)", condominium.toString())
    }

    @Test
    fun `should modify Condominium`() {
        // given
        val address = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = null
        )
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        val condominium = Condominium(id, address)
        // when
        val newAddress = condominium.address.copy(houseNumber = 10)
        condominium.address = newAddress
        condominium.validate()
        // then
        assertEquals(id, condominium.id)
        assertEquals(condominium.address, newAddress)
        assertEquals("Condominium(id=$id, address=$newAddress)", condominium.toString())
    }
}
