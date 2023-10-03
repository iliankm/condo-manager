package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.DeletePersonPort
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
 * Spring test for [DeletePersonPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DeletePersonPersistenceAdapter::class])
class DeletePersonPersistenceAdapterTest {
    @MockkBean
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var deletePersonPort: DeletePersonPort

    @Test
    fun `should delete Person`() {
        // given
        val id = PersonId(UUID.randomUUID().toString(), 5)
        every { personRepository.deleteByDomainId(id) } returns Unit
        // when
        deletePersonPort.delete(id)
        // then
        verifyAll {
            personRepository.deleteByDomainId(id)
        }
    }

    @Test
    fun `should throw IllegalStateException`() {
        // given
        val id = PersonId(UUID.randomUUID().toString())
        // when & then
        assertThrows<IllegalStateException> {
            deletePersonPort.delete(id)
        }
    }
}
