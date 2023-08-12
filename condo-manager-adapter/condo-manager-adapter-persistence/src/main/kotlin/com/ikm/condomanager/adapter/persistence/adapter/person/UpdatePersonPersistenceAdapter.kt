package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.mergeToPersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.port.person.UpdatePersonPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [UpdatePersonPort].
 */
@Component
class UpdatePersonPersistenceAdapter(
    val personRepository: PersonRepository
) : UpdatePersonPort {
    @Transactional
    override fun update(person: Person): Person =
        person.let {
            checkNotNull(it.id)
            personRepository.findByDomainId(it.id!!).orElseThrow {
                NotFoundException("Person with ${it.id} not found.")
            }
        }.let {
            person.mergeToPersonEntity(it)
        }.let {
            personRepository.save(it).convertToPerson()
        }
}
