package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.DeletePersonPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [DeletePersonPort].
 */
@Component
@Transactional
class DeletePersonPersistenceAdapter(
    val personRepository: PersonRepository
) : DeletePersonPort {
    override fun delete(personId: PersonId) {
        checkNotNull(personId.version)
        personRepository.deleteByDomainId(personId)
    }
}
