package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.DeleteCondominiumPort
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [DeleteCondominiumUseCase].
 */
@Service
class DeleteCondominiumService(
    val deleteCondominiumPort: DeleteCondominiumPort
) : DeleteCondominiumUseCase {
    override fun delete(id: CondominiumId) {
        deleteCondominiumPort.delete(id)
    }
}
