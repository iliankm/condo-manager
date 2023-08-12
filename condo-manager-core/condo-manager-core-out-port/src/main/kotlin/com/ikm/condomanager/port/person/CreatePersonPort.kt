package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person

/**
 * Out port for creating a [Person] in the db.
 */
interface CreatePersonPort {
    /**
     * Creates the given valid [person] in the db.
     *
     * @param person the [Person] to be created.
     * @return the created [Person].
     */
    fun create(person: Person): Person
}
