package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.PersonDTO
import com.ikm.condomanager.domain.Person

/**
 * Converts [PersonDTO] to [Person].
 *
 * @return converted instance of [Person]
 */
fun PersonDTO.convertToPerson() =
    Person(id, name).apply {
        email = this@convertToPerson.email
        phoneNumber = this@convertToPerson.phoneNumber
    }

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
 */
fun PersonDTO.mergeToPerson(person: Person) {
    person.also {
        it.name = name
        it.email = email
        it.phoneNumber = phoneNumber
    }
}
