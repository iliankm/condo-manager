package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [LoadCondominiumService].
 */
class LoadCondominiumServiceTest {
    private val loadCondominiumPort = mockk<LoadCondominiumPort>()
    private val loadCondominiumUseCase: LoadCondominiumUseCase = LoadCondominiumService(loadCondominiumPort)

    @Test
    fun `should load Condominium`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString())
        val condominium = mockk<Condominium>()
        every { loadCondominiumPort.load(id) } returns condominium
        // when
        val result = loadCondominiumUseCase.load(id)
        // then
        assertSame(condominium, result)
        verifyAll {
            loadCondominiumPort.load((id))
        }
    }
}
