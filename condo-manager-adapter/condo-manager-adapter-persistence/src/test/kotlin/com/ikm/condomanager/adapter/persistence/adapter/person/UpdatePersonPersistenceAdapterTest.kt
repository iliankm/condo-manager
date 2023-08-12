package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.mergeToPersonEntity
import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.IllegalStateException
import java.util.Optional
import kotlin.test.assertTrue

/**
 * Spring test for [UpdatePersonPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [UpdatePersonPersistenceAdapter::class])
class UpdatePersonPersistenceAdapterTest {
    @MockkBean
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var updatePersonPort: UpdatePersonPort

    @Test
    fun `should update Person`() {
        // given
        mockkStatic(Person::mergeToPersonEntity, PersonEntity::convertToPerson)
        val person = spyk(Person(id = PersonId("person-id", 0), name = "John Doe"))
        val personEntity = mockk<PersonEntity>()
        every { personRepository.findByDomainId(person.id!!) } returns Optional.of(personEntity)
        every { person.mergeToPersonEntity(personEntity) } returns personEntity
        val savedPersonEntity = mockk<PersonEntity>()
        every { personRepository.save(personEntity) } returns savedPersonEntity
        val savedPerson = mockk<Person>()
        every { savedPersonEntity.convertToPerson() } returns savedPerson
        // when
        val result = updatePersonPort.update(person)
        // then
        assertSame(savedPerson, result)
    }

    @Test
    fun `when Person#id is null, should throw IllegalStateException`() {
        // given
        val person = Person(name = "John Doe")
        // when & then
        assertThrows<IllegalStateException> {
            updatePersonPort.update(person)
        }
    }

    @Test
    fun `should throw NotFoundException`() {
        // given
        val id = DomainId("person-id", 0)
        val person = Person(id = id, name = "John Doe")
        every { personRepository.findByDomainId(id) } returns Optional.empty()
        // when
        val ex = assertThrows<NotFoundException> {
            updatePersonPort.update(person)
        }
        // then
        assertTrue(ex.message?.contains("$id not found") ?: false)
    }
}
