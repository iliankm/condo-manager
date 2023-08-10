package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.domain.Person

/**
 * Converts [Person] to [PersonEntity].
 *
 * @return converted instance of [PersonEntity].
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
 * @param personEntity the [PersonEntity] to be merged into.
 * @return the merged instance passed in [personEntity].
 */
fun Person.mergeToPersonEntity(personEntity: PersonEntity) =
    personEntity.let {
        it.name = name
        it.email = email
        it.phoneNumber = phoneNumber
        it
    }

/**
 * Converts [PersonEntity] to [Person].
 */
fun PersonEntity.convertToPerson() =
    Person(
        id = domainId,
        name = name,
        email = email,
        phoneNumber = phoneNumber
    )