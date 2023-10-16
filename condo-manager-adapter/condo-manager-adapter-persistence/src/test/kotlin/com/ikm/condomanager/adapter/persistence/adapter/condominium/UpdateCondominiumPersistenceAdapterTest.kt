package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.converter.mergeToCondominiumEntity
import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

/**
 * Spring test for [UpdateCondominiumPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UpdateCondominiumPersistenceAdapter::class])
class UpdateCondominiumPersistenceAdapterTest {
    @MockkBean
    lateinit var condominiumRepository: CondominiumRepository

    @Autowired
    lateinit var updateCondominiumPort: UpdateCondominiumPort

    @Test
    fun `should update Condominium`() {
        // given
        mockkStatic(Condominium::mergeToCondominiumEntity, CondominiumEntity::convertToCondominium)
        val condominium = Condominium(
            address = CondominiumAddress(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            ),
            id = CondominiumId(UUID.randomUUID().toString(), 1)
        )
        val condominiumEntity = mockk<CondominiumEntity>()
        val updatedCondominium = mockk<Condominium>()
        every { condominiumRepository.getByDomainId(condominium.id!!) } returns condominiumEntity
        every { condominium.mergeToCondominiumEntity(condominiumEntity) } returns Unit
        every { condominiumRepository.saveAndFlush(condominiumEntity) } returns condominiumEntity
        every { condominiumEntity.convertToCondominium() } returns updatedCondominium
        // when
        val result = updateCondominiumPort.update(condominium)
        // then
        assertSame(updatedCondominium, result)
        verifyAll {
            condominiumRepository.getByDomainId(condominium.id!!)
            condominium.mergeToCondominiumEntity(condominiumEntity)
            condominiumRepository.saveAndFlush(condominiumEntity)
            condominiumEntity.convertToCondominium()
        }
    }

    @Test
    fun `when condominium#id is null, should throw IllegalStateException`() {
        // given
        val condominium = Condominium(
            address = CondominiumAddress(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            ),
            id = null
        )
        // when
        assertThrows<IllegalStateException> {
            updateCondominiumPort.update(condominium)
        }
    }

    @Test
    fun `when condominium#id#version is null, should throw IllegalStateException`() {
        // given
        val condominium = Condominium(
            address = CondominiumAddress(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            ),
            id = CondominiumId(UUID.randomUUID().toString(), null)
        )
        // when
        assertThrows<IllegalStateException> {
            updateCondominiumPort.update(condominium)
        }
    }
}
