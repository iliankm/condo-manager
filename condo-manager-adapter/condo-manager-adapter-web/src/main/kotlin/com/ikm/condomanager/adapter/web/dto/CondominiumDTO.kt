package com.ikm.condomanager.adapter.web.dto

import com.ikm.condomanager.domain.CondominiumId

/**
 * DTO for condominium resource.
 */
data class CondominiumDTO(
    val id: CondominiumId,
    val address: CondominiumAddressDTO
)

/**
 * DTO for creation of condominium resource.
 */
data class CondominiumCreateDTO(
    val address: CondominiumAddressDTO
)

/**
 * DTO for updating of condominium resource.
 */
data class CondominiumUpdateDTO(
    val address: CondominiumAddressDTO
)
