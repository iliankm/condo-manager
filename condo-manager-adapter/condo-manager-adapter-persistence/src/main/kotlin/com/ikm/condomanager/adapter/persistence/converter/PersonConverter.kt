package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.domain.Person

/**
 * Converts [Person] to [PersonEntity].
 *
 * @return converted instance of [PersonEntity]
 */
fun Person.convertToPersonEntity() =
    PersonEntity(
        name = name,
        email = email,
        phoneNumber = phoneNumber
    )

/**
 * Merges all possible data from this [Person] to the passed [PersonEntity].
 *
 * @param personEntity the [PersonEntity] to be merged into
 */
fun Person.mergeToPersonEntity(personEntity: PersonEntity) {
    personEntity.also {
        it.name = name
        it.email = email
        it.phoneNumber = phoneNumber
    }
}

/**
 * Converts [PersonEntity] to [Person].
 */
fun PersonEntity.convertToPerson() =
    Person(
        id = domainId,
        name = name
    ).apply {
        email = this@convertToPerson.email
        phoneNumber = this@convertToPerson.phoneNumber
    }
