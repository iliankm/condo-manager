package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.Role.Names.CONDOMINIUM_READ
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import jakarta.annotation.security.RolesAllowed
import org.springframework.stereotype.Service

/**
 * Service implementation of [LoadCondominiumUseCase].
 */
@Service
class LoadCondominiumService(
    val loadCondominiumPort: LoadCondominiumPort
) : LoadCondominiumUseCase {
    @RolesAllowed(CONDOMINIUM_READ)
    override fun load(id: CondominiumId) =
        loadCondominiumPort.load(id)
}
