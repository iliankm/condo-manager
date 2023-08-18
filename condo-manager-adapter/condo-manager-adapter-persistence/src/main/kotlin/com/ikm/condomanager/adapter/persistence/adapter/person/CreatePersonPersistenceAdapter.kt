package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.convertToPersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.CreatePersonPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [CreatePersonPort].
 */
@Component
@Transactional
class CreatePersonPersistenceAdapter(
    val personRepository: PersonRepository
) : CreatePersonPort {
    override fun create(person: Person): Person =
        person.convertToPersonEntity().let {
            personRepository.save(it).convertToPerson()
        }
}
