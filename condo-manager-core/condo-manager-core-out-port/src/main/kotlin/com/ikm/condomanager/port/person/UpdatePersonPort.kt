package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException

/**
 * Out port for updating a [Person] in the db.
 */
interface UpdatePersonPort {
    /**
     * Updates the given valid [person] in the db.
     * [Person.id] must be provided.
     *
     * @param person the [Person] to be updated.
     * @return the updated [Person].
     * @throws NotFoundException if the [Person] to be updated, identified by [Person.id], is not found in the db.
     */
    fun update(person: Person): Person
}
