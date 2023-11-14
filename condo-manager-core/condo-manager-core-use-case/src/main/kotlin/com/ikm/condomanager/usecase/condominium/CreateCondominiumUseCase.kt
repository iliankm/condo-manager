package com.ikm.condomanager.usecase.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.Role.CONDOMINIUM_MANAGE

/**
 * Use case for creating [Condominium].
 */
interface CreateCondominiumUseCase {
    /**
     * Creates [Condominium] in the db.
     *
     * @param condominium valid [Condominium] to be created
     * @return the created [Condominium]
     * @throws AccessDeniedException if the current user doesn't have [CONDOMINIUM_MANAGE] role
     */
    fun create(condominium: Condominium): Condominium
}
