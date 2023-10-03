package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.port.condominium.DeleteCondominiumPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [DeleteCondominiumPort].
 */
@Component
@Transactional
class DeleteCondominiumPersistenceAdapter(
    val condominiumRepository: CondominiumRepository
) : DeleteCondominiumPort {
    override fun delete(id: CondominiumId) {
        checkNotNull(id.version)
        condominiumRepository.deleteByDomainId(id)
    }
}
