package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.application.converter.mergeToCondominium
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import java.util.UUID
import kotlin.test.Test

/**
 * Unit test for [UpdateCondominiumService].
 */
class UpdateCondominiumServiceTest {
    private val loadCondominiumPort = mockk<LoadCondominiumPort>()
    private val updateCondominiumPort = mockk<UpdateCondominiumPort>()
    private val updateCondominiumUseCase: UpdateCondominiumUseCase =
        UpdateCondominiumService(loadCondominiumPort, updateCondominiumPort)

    @Test
    fun `should update Condominium`() {
        // given
        mockkStatic(UpdateCondominiumData::class)
        val condominiumId = CondominiumId(UUID.randomUUID().toString(), 1)
        val updateCondominiumData = mockk<UpdateCondominiumData>()
        val condominium = mockk<Condominium>()
        val updatedCondominium = mockk<Condominium>()
        every { loadCondominiumPort.load(condominiumId) } returns condominium
        every { updateCondominiumData.mergeToCondominium(condominium) } returns Unit
        every { updateCondominiumPort.update(condominium) } returns updatedCondominium
        // when
        val result = updateCondominiumUseCase.update(condominiumId, updateCondominiumData)
        // then
        assertSame(updatedCondominium, result)
        verifyAll {
            loadCondominiumPort.load(condominiumId)
            updateCondominiumData.mergeToCondominium(condominium)
            updateCondominiumPort.update(condominium)
        }
    }

    @Test
    fun `given condominiumId without version, when update, should throw IllegalStateException`() {
        // given
        val condominiumId = CondominiumId(UUID.randomUUID().toString(), null)
        val updateCondominiumData = mockk<UpdateCondominiumData>()
        // when & then
        assertThrows<IllegalStateException> {
            updateCondominiumUseCase.update(condominiumId, updateCondominiumData)
        }
    }
}
