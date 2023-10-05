package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [UpdateCondominiumUseCase].
 */
@Service
class UpdateCondominiumService(
    val updateCondominiumPort: UpdateCondominiumPort
) : UpdateCondominiumUseCase {
    override fun update(condominium: Condominium) =
        updateCondominiumPort.update(condominium)
}