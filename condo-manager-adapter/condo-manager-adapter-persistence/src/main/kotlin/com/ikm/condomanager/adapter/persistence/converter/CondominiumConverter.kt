package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.CondominiumEntity
import com.ikm.condomanager.domain.Condominium

/**
 * Converts [Condominium] to [CondominiumEntity].
 *
 * @return converted instance of [CondominiumEntity].
 */
fun Condominium.convertToCondominiumEntity() =
    CondominiumEntity(
        address = address.convertToCondominiumAddressEntity()
    )

/**
 * Merges all possible data from this [Condominium] to the passed [CondominiumEntity].
 *
 * @param condominiumEntity the [CondominiumEntity] to be merged into.
 * @return the merged instance passed in [condominiumEntity].
 */
fun Condominium.mergeToCondominiumEntity(condominiumEntity: CondominiumEntity) =
    condominiumEntity.let {
        address.mergeToCondominiumAddressEntity(it.address)
        it
    }

/**
 * Converts [CondominiumEntity] to [Condominium].
 */
fun CondominiumEntity.convertToCondominium() =
    Condominium(
        id = domainId,
        address = address.convertToCondominiumAddress()
    )
