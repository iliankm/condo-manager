package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.DeleteCondominiumPort
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [DeleteCondominiumService].
 */
class DeleteCondominiumServiceTest {
    private val deleteCondominiumPort = mockk<DeleteCondominiumPort>()
    private val deleteCondominiumUseCase: DeleteCondominiumUseCase = DeleteCondominiumService(deleteCondominiumPort)

    @Test
    fun `should delete Condominium`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString())
        every { deleteCondominiumPort.delete(id) } returns Unit
        // when
        deleteCondominiumUseCase.delete(id)
        // then
        verifyAll {
            deleteCondominiumPort.delete(id)
        }
    }
}
