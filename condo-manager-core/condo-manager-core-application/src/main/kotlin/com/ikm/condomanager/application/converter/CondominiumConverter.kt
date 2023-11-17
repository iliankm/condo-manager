package com.ikm.condomanager.application.converter

import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData

/**
 * Merges data from this [UpdateCondominiumData] to the passed [condominium].
 *
 * @param condominium the [Condominium] to be merged into
 */
fun UpdateCondominiumData.mergeToCondominium(condominium: Condominium) {
    condominium.also {
        it.address = address
    }
}
