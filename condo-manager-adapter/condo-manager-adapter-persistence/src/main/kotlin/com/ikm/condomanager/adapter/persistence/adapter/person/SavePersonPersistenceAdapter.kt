package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.convertToPersonEntity
import com.ikm.condomanager.adapter.persistence.converter.mergeToPersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.port.person.SavePersonPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [SavePersonPort].
 */
@Component
class SavePersonPersistenceAdapter(
    val personRepository: PersonRepository
) : SavePersonPort {
    @Transactional
    override fun save(person: Person): Person =
        if (person.id == null) {
            person.convertToPersonEntity()
        } else {
            val personEntity = personRepository.findByDomainId(person.id!!)
                .orElseThrow { NotFoundException("Person with ${person.id} not found.") }
            person.mergeToPersonEntity(personEntity)
        }
            .let { personRepository.save(it) }.convertToPerson()
}
