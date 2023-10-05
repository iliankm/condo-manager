package com.ikm.condomanager.usecase.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException

/**
 * Use case for deleting a [Condominium] from the db.
 */
interface DeleteCondominiumUseCase {
    /**
     * Deletes the [Condominium] given by its [id].
     * The [id#version] must be provided.
     *
     * @throws IllegalStateException if [id#version] is not provided
     * @throws NotFoundException if the [Condominium] to be deleted is not found in the db
     * @throws VersionNotMatchedException if [id#version] doesn't match the actual version of the object
     */
    fun delete(id: CondominiumId)
}
