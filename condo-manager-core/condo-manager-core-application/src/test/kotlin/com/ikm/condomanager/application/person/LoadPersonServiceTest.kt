package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.LoadPersonPort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Unit test for [LoadPersonService].
 */
class LoadPersonServiceTest {
    private val loadPersonPort: LoadPersonPort = mockk<LoadPersonPort>()
    private val loadPersonUseCase = LoadPersonService(loadPersonPort)

    @Test
    fun `should load Person by id`() {
        // given
        val personId = PersonId(id = "person-id", version = 2)
        val person = Person(id = personId, name = "John Doe")
        every { loadPersonPort.load(personId) } returns person
        // when
        val res = loadPersonUseCase.load(personId)
        // then
        assertSame(person, res)
    }
}
