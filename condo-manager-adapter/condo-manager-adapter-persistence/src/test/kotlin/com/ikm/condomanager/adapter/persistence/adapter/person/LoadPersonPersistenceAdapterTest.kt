package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional
import kotlin.test.assertSame
import kotlin.test.assertTrue

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
        val id = DomainId("person-id", 0)
        val personEntity = mockk<PersonEntity>()
        val person = Person(id = id, name = "John Doe")
        every { personEntity.convertToPerson() } returns person
        every { personRepository.findByDomainId(id) } returns Optional.of(personEntity)
        // when
        val result = loadPersonPort.load(id)
        // then
        assertSame(person, result)
    }

    @Test
    fun `should throw NotFoundException`() {
        // given
        val id = DomainId("person-id", 0)
        every { personRepository.findByDomainId(id) } returns Optional.empty()
        // when
        val ex = assertThrows<NotFoundException> {
            loadPersonPort.load(id)
        }
        // then
        assertTrue(ex.message?.contains("$id not found") ?: false)
    }
}
