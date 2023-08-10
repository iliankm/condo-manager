package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException

/**
 * Out port for saving a [Person] in the db.
 */
interface SavePersonPort {
    /**
     * Saves the given valid [person] in the db.
     * If [Person.id] is null, it's inserted in the db, otherwise it's updated.
     *
     * @param person the [Person] to be saved.
     * @return the saved [Person].
     * @throws NotFoundException if the [Person] to be updated, identified by [Person.id], is not found in the db.
     */
    fun save(person: Person): Person
}