package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.LoadCondominiumPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [LoadCondominiumPort].
 */
@Component
@Transactional
class LoadCondominiumPersistenceAdapter(
    val condominiumRepository: CondominiumRepository
) : LoadCondominiumPort {
    override fun load(id: CondominiumId) =
        condominiumRepository.getByDomainId(id).convertToCondominium()
}
