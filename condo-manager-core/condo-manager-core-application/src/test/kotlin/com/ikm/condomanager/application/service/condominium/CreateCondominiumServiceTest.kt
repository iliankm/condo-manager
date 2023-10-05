package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.CreateCondominiumPort
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Unit test for [CreateCondominiumService].
 */
class CreateCondominiumServiceTest {
    private val createCondominiumPort = mockk<CreateCondominiumPort>()
    private val createCondominiumUseCase: CreateCondominiumUseCase = CreateCondominiumService(createCondominiumPort)

    @Test
    fun `should create Condominium`() {
        // given
        val condominium = mockk<Condominium>()
        val createdCondominium = mockk<Condominium>()
        every { createCondominiumPort.create(condominium) } returns createdCondominium
        // when
        val result = createCondominiumUseCase.create(condominium)
        // then
        assertSame(createdCondominium, result)
        verifyAll {
            createCondominiumPort.create(condominium)
        }
    }
}
