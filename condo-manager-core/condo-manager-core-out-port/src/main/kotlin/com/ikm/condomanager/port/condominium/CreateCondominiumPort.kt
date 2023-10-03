package com.ikm.condomanager.port.condominium

import com.ikm.condomanager.domain.Condominium

/**
 * Out port for creating [Condominium] in the db.
 */
interface CreateCondominiumPort {
    /**
     * Creates [Condominium] in the db.
     *
     * @param condominium valid [Condominium] to be created.
     * @return the created [Condominium].
     */
    fun create(condominium: Condominium): Condominium
}
