package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.port.person.UpdatePersonPort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Unit test for [SavePersonService].
 */
class SavePersonServiceTest {
    private val createPersonPort = mockk<CreatePersonPort>()
    private val updatePersonPort = mockk<UpdatePersonPort>()
    private val savePersonUseCase = SavePersonService(createPersonPort, updatePersonPort)

    @Test
    fun `should create Person`() {
        // given
        val person = Person(id = null, name = "John Doe")
        val createdPerson = Person(id = PersonId("person-id", 0), name = "John Doe")
        every { createPersonPort.create(person) } returns createdPerson
        // when
        val res = savePersonUseCase.save(person)
        // then
        assertSame(createdPerson, res)
    }

    @Test
    fun `should update Person`() {
        // given
        val person = Person(id = PersonId("person-id", 1), name = "John Doe")
        val updatedPerson = Person(id = PersonId("person-id", 1), name = "John Doe")
        every { updatePersonPort.update(person) } returns updatedPerson
        // when
        val res = savePersonUseCase.save(person)
        // then
        assertSame(updatedPerson, res)
    }
}
