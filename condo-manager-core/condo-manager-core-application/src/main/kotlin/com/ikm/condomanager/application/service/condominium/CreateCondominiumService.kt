package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.Role.Names.CONDOMINIUM_MANAGE
import com.ikm.condomanager.port.condominium.CreateCondominiumPort
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import jakarta.annotation.security.RolesAllowed
import org.springframework.stereotype.Service

/**
 * Service implementation of [CreateCondominiumUseCase].
 */
@Service
class CreateCondominiumService(
    val createCondominiumPort: CreateCondominiumPort
) : CreateCondominiumUseCase {
    @RolesAllowed(CONDOMINIUM_MANAGE)
    override fun create(condominium: Condominium) =
        createCondominiumPort.create(condominium)
}
