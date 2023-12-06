package com.ikm.condomanager.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

/**
 * Value object representing a condominium address.
 *
 * @property city address city. must be not blank and max size is 50.
 * @property street address street name. must be not blank and max size is 200.
 * @property houseNumber address house number. must be positive integer not bigger than 32767.
 * @property location geolocation.
 */
data class CondominiumAddress(
    @field:NotBlank
    @field:Size(max = 50)
    val city: String,
    @field:NotBlank
    @field:Size(max = 200)
    val street: String,
    @field:Positive
    val houseNumber: Short,
    val location: GeoLocation?
) : SelfValidating {
    init {
        validate()
    }
}
