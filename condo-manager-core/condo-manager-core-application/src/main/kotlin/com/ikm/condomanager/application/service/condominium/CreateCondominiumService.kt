package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.CreateCondominiumPort
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [CreateCondominiumUseCase].
 */
@Service
class CreateCondominiumService(
    val createCondominiumPort: CreateCondominiumPort
) : CreateCondominiumUseCase {
    override fun create(condominium: Condominium) =
        createCondominiumPort.create(condominium)
}
