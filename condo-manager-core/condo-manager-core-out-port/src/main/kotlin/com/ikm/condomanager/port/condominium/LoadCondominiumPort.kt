package com.ikm.condomanager.port.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.exception.NotFoundException

/**
 * Out port for loading [Condominium] from the db.
 */
interface LoadCondominiumPort {
    /**
     * Finds and loads a [Condominium] by given [id].
     *
     * @param id the id of the [Condominium] to be loaded.
     * @return the requested [Condominium].
     * @throws NotFoundException if the requested [Condominium] is not found.
     */
    fun load(id: CondominiumId): Condominium
}
