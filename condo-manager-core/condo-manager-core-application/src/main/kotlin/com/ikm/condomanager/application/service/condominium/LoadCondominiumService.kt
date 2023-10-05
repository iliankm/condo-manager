package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [LoadCondominiumUseCase].
 */
@Service
class LoadCondominiumService(
    val loadCondominiumPort: LoadCondominiumPort
) : LoadCondominiumUseCase {
    override fun load(id: CondominiumId) =
        loadCondominiumPort.load(id)
}
