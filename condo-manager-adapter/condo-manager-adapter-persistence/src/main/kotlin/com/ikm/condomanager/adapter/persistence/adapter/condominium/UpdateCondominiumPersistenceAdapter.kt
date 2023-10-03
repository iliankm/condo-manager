package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.converter.mergeToCondominiumEntity
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.UpdateCondominiumPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [UpdateCondominiumPort].
 */
@Component
@Transactional
class UpdateCondominiumPersistenceAdapter(
    val condominiumRepository: CondominiumRepository
) : UpdateCondominiumPort {
    override fun update(condominium: Condominium) =
        condominium.let {
            checkNotNull(it.id)
            checkNotNull(it.id!!.version)
            condominiumRepository.getByDomainId(it.id!!)
        }.let {
            condominium.mergeToCondominiumEntity(it)
            condominiumRepository.saveAndFlush(it).convertToCondominium()
        }
}
