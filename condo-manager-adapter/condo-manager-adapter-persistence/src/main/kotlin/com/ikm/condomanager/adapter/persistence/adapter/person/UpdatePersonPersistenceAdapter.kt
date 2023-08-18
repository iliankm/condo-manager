package com.ikm.condomanager.adapter.persistence.adapter.person

import com.ikm.condomanager.adapter.persistence.converter.convertToPerson
import com.ikm.condomanager.adapter.persistence.converter.mergeToPersonEntity
import com.ikm.condomanager.adapter.persistence.repository.PersonRepository
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.UpdatePersonPort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * Persistence adapter implementation of [UpdatePersonPort].
 */
@Component
@Transactional
class UpdatePersonPersistenceAdapter(
    val personRepository: PersonRepository
) : UpdatePersonPort {
    override fun update(person: Person): Person =
        person.let {
            checkNotNull(it.id)
            checkNotNull(it.id!!.version)
            personRepository.getByDomainId(it.id!!)
        }.let {
            person.mergeToPersonEntity(it)
        }.let {
            personRepository.saveAndFlush(it).convertToPerson()
        }
}
