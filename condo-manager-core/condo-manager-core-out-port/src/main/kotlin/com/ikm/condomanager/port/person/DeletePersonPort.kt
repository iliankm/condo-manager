package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException

/**
 * Out port for deleting a [Person] from the db.
 */
interface DeletePersonPort {
    /**
     * Deletes the [Person] given by its [personId].
     * The [personId#version] must be provided.
     *
     * @throws IllegalStateException if [personId#version] is not provided
     * @throws NotFoundException if the [Person] to be deleted, identified by [personId], is not found in the db
     * @throws VersionNotMatchedException if [personId#version] doesn't match the actual version of the object
     */
    fun delete(personId: PersonId)
}
