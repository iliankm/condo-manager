package com.ikm.condomanager.port.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.exception.NotFoundException

/**
 * Out port for loading a [Person] from the db.
 */
interface LoadPersonPort {
    /**
     * Finds and loads a [Person] by given [id].
     *
     * @param id the id of the [Person] to be loaded.
     * @return the requested [Person].
     * @throws NotFoundException if the requested [Person] is not found.
     */
    fun load(id: PersonId): Person
}
