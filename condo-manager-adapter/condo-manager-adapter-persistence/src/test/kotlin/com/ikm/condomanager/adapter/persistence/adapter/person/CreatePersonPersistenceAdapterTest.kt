package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.convertToPersonEntity
import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Spring test for [CreatePersonPersistenceAdapter].
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CreatePersonPersistenceAdapter::class])
class CreatePersonPersistenceAdapterTest {
    @MockkBean
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var createPersonPort: CreatePersonPort

    @Test
    fun `should create Person`() {
        // given
        mockkStatic(Person::convertToPersonEntity, PersonEntity::convertToPerson)
        val person = spyk(Person(id = null, name = "John Doe"))
        val personEntity = PersonEntity(name = "John Doe")
        every { person.convertToPersonEntity() } returns personEntity
        val savedPersonEntity = mockk<PersonEntity>()
        every { personRepository.save(personEntity) } returns savedPersonEntity
        val savedPerson = mockk<Person>()
        every { savedPersonEntity.convertToPerson() } returns savedPerson
        // when
        val result = createPersonPort.create(person)
        // then
        assertSame(savedPerson, result)
        verifyAll {
            person.convertToPersonEntity()
            personRepository.save(personEntity)
            savedPersonEntity.convertToPerson()
        }
    }
}
