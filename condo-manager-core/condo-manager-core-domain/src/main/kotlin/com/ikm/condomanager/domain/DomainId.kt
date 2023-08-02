package com.ikm.condomanager.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

/**
 * Base value object representing an id of a domain entity or aggregate.
 *
 * @property id the id of the domain entity. Must be not blank.
 * @property version the version of the domain entity. Must be positive or zero number.
 */
data class DomainId(
    @field:NotBlank
    val id: String,

    @field:PositiveOrZero
    val version: Long
) : SelfValidating<DomainId>() {
    init {
        validate()
    }
}
