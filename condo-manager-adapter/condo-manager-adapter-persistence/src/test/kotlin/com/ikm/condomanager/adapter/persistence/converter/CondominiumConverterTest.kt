package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.CondominiumAddressEntity
import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.CondominiumId
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [Condominium] related conversions.
 */
class CondominiumConverterTest {

    @Test
    fun `should convert Condominium to CondominiumEntity`() {
        // given
        mockkStatic(CondominiumAddress::convertToCondominiumAddressEntity)
        val condominium = Condominium(
            id = CondominiumId(UUID.randomUUID().toString()),
            address = mockk<CondominiumAddress>()
        )
        val condominiumAddressEntity = mockk<CondominiumAddressEntity>()
        every { condominium.address.convertToCondominiumAddressEntity() } returns condominiumAddressEntity
        // when
        val condominiumEntity = condominium.convertToCondominiumEntity()
        // then
        assertNull(condominiumEntity.id)
        assertSame(condominiumAddressEntity, condominiumEntity.address)
        verifyAll {
            condominium.address.convertToCondominiumAddressEntity()
        }
    }

    @Test
    fun `should merge Condominium to CondominiumEntity`() {
        // given
        mockkStatic(CondominiumAddress::mergeToCondominiumAddressEntity)
        val condominium = Condominium(
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            address = mockk<CondominiumAddress>()
        )
        val condominiumEntity = CondominiumEntity(
            address = mockk<CondominiumAddressEntity>()
        )
        every { condominium.address.mergeToCondominiumAddressEntity(condominiumEntity.address) } returns Unit
        // when
        condominium.mergeToCondominiumEntity(condominiumEntity)
        // then
        verifyAll {
            condominium.address.mergeToCondominiumAddressEntity(condominiumEntity.address)
        }
    }

    @Test
    fun `should convert CondominiumEntity to Condominium`() {
        // given
        mockkStatic(CondominiumAddressEntity::convertToCondominiumAddress)
        val condominiumEntity = CondominiumEntity(
            address = mockk<CondominiumAddressEntity>()
        )
        val condominiumAddress = mockk<CondominiumAddress>()
        every { condominiumEntity.address.convertToCondominiumAddress() } returns condominiumAddress
        // when
        val condominium = condominiumEntity.convertToCondominium()
        // then
        assertSame(condominiumAddress, condominium.address)
        verifyAll {
            condominiumEntity.address.convertToCondominiumAddress()
        }
    }
}
