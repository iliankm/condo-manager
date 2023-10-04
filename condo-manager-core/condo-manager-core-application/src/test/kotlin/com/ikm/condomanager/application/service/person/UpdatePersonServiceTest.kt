package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.UpdatePersonPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [CreatePersonService].
 */
class UpdatePersonServiceTest {
    private val updatePersonPort = mockk<UpdatePersonPort>()
    private val updatePersonService = UpdatePersonService(updatePersonPort)

    @Test
    fun `should update Person`() {
        // given
        val id = UUID.randomUUID().toString()
        val person = Person(id = PersonId(id, 1), name = "John Doe")
        val updatedPerson = Person(id = PersonId(id, 1), name = "John Doe")
        every { updatePersonPort.update(person) } returns updatedPerson
        // when
        val res = updatePersonService.update(person)
        // then
        assertSame(updatedPerson, res)
        verifyAll {
            updatePersonPort.update(person)
        }
    }
}
