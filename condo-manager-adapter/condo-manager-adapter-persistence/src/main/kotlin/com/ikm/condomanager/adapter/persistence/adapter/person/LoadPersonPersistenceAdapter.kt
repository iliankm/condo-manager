package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.LoadPersonPort
import org.springframework.stereotype.Component

/**
 * Persistence adapter implementation of [LoadPersonPort].
 */
@Component
class LoadPersonPersistenceAdapter(
    val personRepository: PersonRepository
) : LoadPersonPort {
    override fun load(id: PersonId) =
        personRepository.getByDomainId(id).convertToPerson()
}
