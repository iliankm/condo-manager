package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
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
 * Unit test for [CondominiumDTO] related conversions.
 */
class CondominiumDTOConverterTest {
    @Test
    fun `should convert CondominiumCreateDTO to Condominium`() {
        // given
        mockkStatic(CondominiumAddressDTO::convertToCondominiumAddress)
        val condominiumAddressDTO = mockk<CondominiumAddressDTO>()
        val condominiumDTO = CondominiumDTO(
            id = null,
            address = condominiumAddressDTO
        )
        val condominiumAddress = mockk<CondominiumAddress>()
        every { condominiumAddressDTO.convertToCondominiumAddress() } returns condominiumAddress
        // when
        val condominium = condominiumDTO.convertToCondominium()
        // then
        assertNull(condominium.id)
        assertSame(condominiumAddress, condominium.address)
        verifyAll {
            condominiumAddressDTO.convertToCondominiumAddress()
        }
    }

    @Test
    fun `should convert Condominium to CondominiumDTO`() {
        // given
        mockkStatic(CondominiumAddress::convertToCondominiumAddressDTO)
        val condominiumAddress = mockk<CondominiumAddress>()
        val condominium = Condominium(
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            address = condominiumAddress
        )
        val condominiumAddressDTO = mockk<CondominiumAddressDTO>()
        every { condominiumAddress.convertToCondominiumAddressDTO() } returns condominiumAddressDTO
        // when
        val condominiumDTO = condominium.convertToCondominiumDTO()
        // then
        assertSame(condominium.id, condominiumDTO.id)
        assertSame(condominiumAddressDTO, condominiumDTO.address)
        verifyAll {
            condominiumAddress.convertToCondominiumAddressDTO()
        }
    }

    @Test
    fun `should convert CondominiumDTO to UpdateCondominiumData`() {
        // given
        mockkStatic(CondominiumAddressDTO::convertToCondominiumAddress)
        val condominiumAddressDTO = mockk<CondominiumAddressDTO>()
        val condominiumDTO = CondominiumDTO(
            id = CondominiumId(UUID.randomUUID().toString(), 0),
            address = condominiumAddressDTO
        )
        val condominiumAddress = mockk<CondominiumAddress>()
        every { condominiumAddressDTO.convertToCondominiumAddress() } returns condominiumAddress
        // when
        val updateCondominiumData = condominiumDTO.convertToUpdateCondominiumData()
        // then
        assertSame(condominiumAddress, updateCondominiumData.address)
    }
}
