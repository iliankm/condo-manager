package com.ikm.condomanager.usecase.condominium

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumAddress
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.Role.CONDOMINIUM_MANAGE
import com.ikm.condomanager.domain.SelfValidating
import com.ikm.condomanager.domain.validate
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException

/**
 * Use case for updating [Condominium].
 */
interface UpdateCondominiumUseCase {
    /**
     * Updates [Condominium] in the db.
     * [condominiumId#version] must be provided.
     *
     * @param condominiumId the [Condominium] id to be updated
     * @param updateCondominiumData the data to be updated
     * @return the updated [Condominium]
     * @throws AccessDeniedException if the current user doesn't have [CONDOMINIUM_MANAGE] role
     * @throws IllegalStateException if [condominium#id] or [condominium#id#version] are not provided
     * @throws NotFoundException if the [Condominium] to be updated is not found in the db
     * @throws VersionNotMatchedException if [condominium#id#version] doesn't match the actual version of the object
     */
    fun update(condominiumId: CondominiumId, updateCondominiumData: UpdateCondominiumData): Condominium
}

/**
 * Object holding the [Condominium] data available for update.
 */
data class UpdateCondominiumData(
    val address: CondominiumAddress
) : SelfValidating {
    init {
        validate()
    }
}
