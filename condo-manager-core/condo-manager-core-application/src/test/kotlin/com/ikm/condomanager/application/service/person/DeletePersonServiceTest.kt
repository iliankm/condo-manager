package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.DeletePersonPort
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [DeletePersonService].
 */
class DeletePersonServiceTest {
    private val deletePersonPort = mockk<DeletePersonPort>()
    private val deletePersonUseCase = DeletePersonService(deletePersonPort)

    @Test
    fun `should delete Person`() {
        // given
        val id = PersonId(UUID.randomUUID().toString(), 10)
        every { deletePersonPort.delete(id) } returns Unit
        // when & then
        deletePersonUseCase.delete(id)
    }
}
