package com.ikm.condomanager.domain

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

/**
 * Unit test for [Condominium].
 */
class CondominiumTest {
    @Test
    fun `should create Condominium`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString())
        val address = CondominiumAddress(
            city = "City Name",
            street = "Street Name",
            houseNumber = 1,
            location = null
        )
        // when
        val condominium = Condominium(id, address)
        // then
        assertEquals(id, condominium.id)
        assertEquals(condominium.address, address)
        assertEquals("Condominium(id=$id, address=$address)", condominium.toString())
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
