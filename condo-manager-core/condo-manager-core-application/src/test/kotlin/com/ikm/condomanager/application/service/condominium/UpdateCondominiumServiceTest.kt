package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import kotlin.test.Test

/**
 * Unit test for [UpdateCondominiumService].
 */
class UpdateCondominiumServiceTest {
    private val updateCondominiumPort = mockk<UpdateCondominiumPort>()
    private val updateCondominiumUseCase: UpdateCondominiumUseCase = UpdateCondominiumService(updateCondominiumPort)

    @Test
    fun `should update Condominium`() {
        // given
        val condominium = mockk<Condominium>()
        val updatedCondominium = mockk<Condominium>()
        every { updateCondominiumPort.update(condominium) } returns updatedCondominium
        // when
        val result = updateCondominiumUseCase.update(condominium)
        // then
        assertSame(updatedCondominium, result)
        verifyAll {
            updateCondominiumPort.update(condominium)
        }
    }
}
