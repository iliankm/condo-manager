package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID
import kotlin.test.Test

/**
 * Spring test for [LoadCondominiumPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LoadCondominiumPersistenceAdapter::class])
class LoadCondominiumPersistenceAdapterTest {
    @MockkBean
    lateinit var condominiumRepository: CondominiumRepository

    @Autowired
    lateinit var loadCondominiumPort: LoadCondominiumPort

    @Test
    fun `should load Condominium by id`() {
        // given
        mockkStatic(CondominiumEntity::convertToCondominium)
        val id = CondominiumId(UUID.randomUUID().toString())
        val condominiumEntity = mockk<CondominiumEntity>()
        val condominium = mockk<Condominium>()
        every { condominiumRepository.getByDomainId(id) } returns condominiumEntity
        every { condominiumEntity.convertToCondominium() } returns condominium
        // when
        val result = loadCondominiumPort.load(id)
        // then
        assertSame(condominium, result)
        verifyAll {
            condominiumRepository.getByDomainId(id)
            condominiumEntity.convertToCondominium()
        }
    }
}
