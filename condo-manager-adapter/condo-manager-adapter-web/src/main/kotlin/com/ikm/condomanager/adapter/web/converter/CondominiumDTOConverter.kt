package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.CondominiumCreateDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumUpdateDTO
import com.ikm.condomanager.domain.Condominium

/**
 * Converts [CondominiumCreateDTO] to [Condominium].
 *
 * @return converted instance of [Condominium]
 */
fun CondominiumCreateDTO.convertToCondominium() =
    Condominium.create(
        address = address.convertToCondominiumAddress()
    )

/**
 * Converts [Condominium] to [CondominiumDTO].
 *
 * @return converted instance of [CondominiumDTO]
 */
fun Condominium.convertToCondominiumDTO() =
    CondominiumDTO(
        id = id!!,
        address = address.convertToCondominiumAddressDTO()
    )

/**
 * Merges this [CondominiumUpdateDTO] to the passed [Condominium].
 *
 * @param condominium the [Condominium] instance to be merged into
 */
fun CondominiumUpdateDTO.mergeToCondominium(condominium: Condominium) {
    condominium.also {
        condominium.address = address.convertToCondominiumAddress()
    }
}
