package com.ikm.condomanager.usecase.condominium

import com.ikm.condomanager.domain.Condominium

/**
 * Use case for creating [Condominium].
 */
interface CreateCondominiumUseCase {
    /**
     * Creates [Condominium] in the db.
     *
     * @param condominium valid [Condominium] to be created
     * @return the created [Condominium]
     */
    fun create(condominium: Condominium): Condominium
}
