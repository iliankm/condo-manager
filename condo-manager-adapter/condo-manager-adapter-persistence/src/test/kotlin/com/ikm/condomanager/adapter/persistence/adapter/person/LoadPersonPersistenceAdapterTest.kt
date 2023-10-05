package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID
import kotlin.test.assertSame

/**
 * Spring test for [LoadPersonPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LoadPersonPersistenceAdapter::class])
class LoadPersonPersistenceAdapterTest {
    @MockkBean
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var loadPersonPort: LoadPersonPort

    @Test
    fun `should load Person by id`() {
        // given
        mockkStatic(PersonEntity::convertToPerson)
        val id = DomainId(UUID.randomUUID().toString(), 0)
        val personEntity = mockk<PersonEntity>()
        val person = Person(id = id, name = "John Doe", email = null, phoneNumber = null)
        every { personEntity.convertToPerson() } returns person
        every { personRepository.getByDomainId(id) } returns personEntity
        // when
        val result = loadPersonPort.load(id)
        // then
        assertSame(person, result)
        verifyAll {
            personEntity.convertToPerson()
            personRepository.getByDomainId(id)
        }
    }
}
