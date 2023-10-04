package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.converter.convertToCondominiumEntity
import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.CreateCondominiumPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Spring test for [CreateCondominiumPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CreateCondominiumPersistenceAdapter::class])
class CreateCondominiumPersistenceAdapterTest {
    @MockkBean
    lateinit var condominiumRepository: CondominiumRepository

    @Autowired
    lateinit var createCondominiumPort: CreateCondominiumPort

    @Test
    fun `should create Condominium`() {
        // given
        mockkStatic(Condominium::convertToCondominiumEntity, CondominiumEntity::convertToCondominium)
        val createData = mockk<Condominium>()
        val condominiumEntity = mockk<CondominiumEntity>()
        every { createData.convertToCondominiumEntity() } returns condominiumEntity
        every { condominiumRepository.save(condominiumEntity) } returns condominiumEntity
        val createdCondominium = mockk<Condominium>()
        every { condominiumEntity.convertToCondominium() } returns createdCondominium
        // when
        val result = createCondominiumPort.create(createData)
        // then
        assertSame(createdCondominium, result)
        verifyAll {
            createData.convertToCondominiumEntity()
            condominiumRepository.save(condominiumEntity)
            condominiumEntity.convertToCondominium()
        }
    }
}
