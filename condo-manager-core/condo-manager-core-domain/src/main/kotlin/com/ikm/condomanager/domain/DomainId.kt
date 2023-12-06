package com.ikm.condomanager.domain

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.PositiveOrZero

/**
 * Base value object representing an id of a domain entity or aggregate.
 *
 * @property id the id of the domain entity. Must be not blank.
 * @property version the version of the domain entity. Must be positive or zero number.
 */
data class DomainId(
    @field:NotBlank
    @field:Pattern(
        regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
        message = "Must be a valid UUID"
    )
    val id: String,

    @field:PositiveOrZero
    val version: Long? = null
) : SelfValidating {
    init {
        validate()
    }
}
