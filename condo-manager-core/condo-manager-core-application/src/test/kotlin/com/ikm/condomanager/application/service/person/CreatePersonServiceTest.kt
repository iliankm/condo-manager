package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.CreatePersonPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [CreatePersonService].
 */
class CreatePersonServiceTest {
    private val createPersonPort = mockk<CreatePersonPort>()
    private val createPersonUseCase = CreatePersonService(createPersonPort)

    @Test
    fun `should create Person`() {
        // given
        val person = Person(id = null, name = "John Doe")
        val createdPerson = Person(id = PersonId(UUID.randomUUID().toString(), 0), name = "John Doe")
        every { createPersonPort.create(person) } returns createdPerson
        // when
        val res = createPersonUseCase.create(person)
        // then
        assertSame(createdPerson, res)
        verifyAll {
            createPersonPort.create(person)
        }
    }
}
