package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.DeleteCondominiumPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

/**
 * Spring test for [DeleteCondominiumPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DeleteCondominiumPersistenceAdapter::class])
class DeleteCondominiumPersistenceAdapterTest {
    @MockkBean
    lateinit var condominiumRepository: CondominiumRepository

    @Autowired
    lateinit var deleteCondominiumPort: DeleteCondominiumPort

    @Test
    fun `should delete Condominium`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        every { condominiumRepository.deleteByDomainId(id) } returns Unit
        // when
        deleteCondominiumPort.delete(id)
        // then
        verifyAll {
            condominiumRepository.deleteByDomainId(id)
        }
    }

    @Test
    fun `should throw IllegalStateException`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString())
        // when & then
        assertThrows<IllegalStateException> {
            deleteCondominiumPort.delete(id)
        }
    }
}
