package com.ikm.condomanager.adapter.web.dto

/**
 * DTO for condominium address resource.
 */
data class CondominiumAddressDTO(
    val city: String,
    val street: String,
    val houseNumber: Short,
    val location: GeoLocationDTO?
)
