package com.ikm.condomanager.port.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException

/**
 * Out port for updating a [Condominium] in the db.
 */
interface UpdateCondominiumPort {
    /**
     * Updates [Condominium] in the db.
     * [condominium#id] and [condominium#id#version] must be provided.
     *
     * @param condominium the [Condominium] to be updated
     * @return the updated [Condominium]
     * @throws IllegalStateException if [condominium#id] or [condominium#id#version] are not provided
     * @throws NotFoundException if the [Condominium] to be updated is not found in the db
     * @throws VersionNotMatchedException if [condominium#id#version] doesn't match the actual version of the object
     */
    fun update(condominium: Condominium): Condominium
}
