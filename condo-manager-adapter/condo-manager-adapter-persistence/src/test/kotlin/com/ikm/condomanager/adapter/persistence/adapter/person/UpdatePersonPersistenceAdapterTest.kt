package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.mergeToPersonEntity
import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.UpdatePersonPort
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
        val person = Person(
            id = PersonId(UUID.randomUUID().toString(), 1),
            name = "John Doe",
            email = null,
            phoneNumber = null
        )
        val personEntity = mockk<PersonEntity>()
        every { personRepository.getByDomainId(person.id!!) } returns personEntity
        every { person.mergeToPersonEntity(personEntity) } returns Unit
        every { personRepository.saveAndFlush(personEntity) } returns personEntity
        val savedPerson = mockk<Person>()
        every { personEntity.convertToPerson() } returns savedPerson
        // when
        val result = updatePersonPort.update(person)
        // then
        assertSame(savedPerson, result)
        verifyAll {
            personRepository.getByDomainId(person.id!!)
            person.mergeToPersonEntity(personEntity)
            personRepository.saveAndFlush(personEntity)
            personEntity.convertToPerson()
        }
    }

    @Test
    fun `when Person#id is null, should throw IllegalStateException`() {
        // given
        val person = Person(id = null, name = "John Doe", email = null, phoneNumber = null)
        // when & then
        assertThrows<IllegalStateException> {
            updatePersonPort.update(person)
        }
    }

    @Test
    fun `when Person#id#version is null, should throw IllegalStateException`() {
        // given
        val person =
            Person(id = PersonId(UUID.randomUUID().toString()), name = "John Doe", email = null, phoneNumber = null)
        // when & then
        assertThrows<IllegalStateException> {
            updatePersonPort.update(person)
        }
    }
}
