package com.ikm.condomanager.application.service.condominium

import com.ikm.condomanager.application.converter.mergeToCondominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.Role.Names.CONDOMINIUM_MANAGE
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import jakarta.annotation.security.RolesAllowed
import org.springframework.stereotype.Service

/**
 * Service implementation of [UpdateCondominiumUseCase].
 */
@Service
class UpdateCondominiumService(
    val loadCondominiumPort: LoadCondominiumPort,
    val updateCondominiumPort: UpdateCondominiumPort
) : UpdateCondominiumUseCase {
    @RolesAllowed(CONDOMINIUM_MANAGE)
    override fun update(condominiumId: CondominiumId, updateCondominiumData: UpdateCondominiumData) =
        condominiumId.let {
            checkNotNull(it.version)
            loadCondominiumPort.load(condominiumId)
        }.let {
            updateCondominiumData.mergeToCondominium(it)
            updateCondominiumPort.update(it)
        }
}
