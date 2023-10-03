package com.ikm.condomanager.adapter.persistence.adapter.condominium

import com.ikm.condomanager.adapter.persistence.converter.convertToCondominium
import com.ikm.condomanager.adapter.persistence.converter.convertToCondominiumEntity
import com.ikm.condomanager.adapter.persistence.repository.CondominiumRepository
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.port.condominium.CreateCondominiumPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [CreateCondominiumPort].
 */
@Component
@Transactional
class CreateCondominiumPersistenceAdapter(
    val condominiumRepository: CondominiumRepository
) : CreateCondominiumPort {
    override fun create(condominium: Condominium): Condominium =
        condominium.convertToCondominiumEntity().let {
            condominiumRepository.save(it).convertToCondominium()
        }
}
