package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData

/**
 * Converts [CondominiumDTO] to [Condominium].
 *
 * @return converted instance of [Condominium]
 */
fun CondominiumDTO.convertToCondominium() =
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
 * Converts [CondominiumDTO] to [UpdateCondominiumData].
 */
fun CondominiumDTO.convertToUpdateCondominiumData() =
    UpdateCondominiumData(
        address = address.convertToCondominiumAddress()
    )
