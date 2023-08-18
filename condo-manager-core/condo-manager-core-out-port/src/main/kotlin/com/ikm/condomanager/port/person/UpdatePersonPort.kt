package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException

/**
 * Out port for updating a [Person] in the db.
 */
interface UpdatePersonPort {
    /**
     * Updates the given valid [person] in the db.
     * [person#id] and [person#id#version] must be provided.
     *
     * @param person the [Person] to be updated
     * @return the updated [Person]
     * @throws IllegalStateException if [person#id] or [person#id#version] are not provided
     * @throws NotFoundException if the [Person] to be updated, identified by [Person.id], is not found in the db
     * @throws VersionNotMatchedException if [person#id#version] doesn't match the actual version of the object
     */
    fun update(person: Person): Person
}
