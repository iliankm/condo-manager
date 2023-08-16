package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.controller.PersonDTO
import com.ikm.condomanager.domain.Person

/**
 * Converts [PersonDTO] to [Person].
 *
 * @return converted instance of [Person]
 */
fun PersonDTO.convertToPerson() =
    Person(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber
    )

/**
 * Converts [Person] to [PersonDTO].
 *
 * @return converted instance of [PersonDTO]
 */
fun Person.convertToPersonDTO() =
    PersonDTO(
        id = id,
        name = name,
        email = email,
        phoneNumber = phoneNumber
    )

/**
 * Merges all possible data from this [PersonDTO] to the passed [Person].
 *
 * @param person the [Person] instance to be merged into
 * @return the merged instance passed in [person]
 */
fun PersonDTO.mergeToPerson(person: Person) =
    person.let {
        it.name = name
        it.email = email
        it.phoneNumber = phoneNumber
        it
    }
