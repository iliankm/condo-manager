package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.Role.Names.CONDOMINIUM_MANAGE
import com.ikm.condomanager.port.condominium.DeleteCondominiumPort
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import jakarta.annotation.security.RolesAllowed
import org.springframework.stereotype.Service

/**
 * Service implementation of [DeleteCondominiumUseCase].
 */
@Service
class DeleteCondominiumService(
    val deleteCondominiumPort: DeleteCondominiumPort
) : DeleteCondominiumUseCase {
    @RolesAllowed(CONDOMINIUM_MANAGE)
    override fun delete(id: CondominiumId) {
        checkNotNull(id.version)
        deleteCondominiumPort.delete(id)
    }
}
