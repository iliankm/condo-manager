package com.ikm.condomanager.adapter.web.dto

import com.ikm.condomanager.domain.CondominiumId

/**
 * DTO for condominium resource.
 */
data class CondominiumDTO(
    val id: CondominiumId?,
    val address: CondominiumAddressDTO
)
