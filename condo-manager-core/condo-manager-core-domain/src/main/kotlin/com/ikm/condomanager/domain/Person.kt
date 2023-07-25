package com.ikm.condomanager.domain

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * id of a [Person].
 */
typealias PersonId = DomainId

/**
 * Domain object representing a [Person].
 */
class Person(
    @field:Valid
    val id: PersonId,

    @field:NotBlank
    @field:Size(max = 70)
    var name: String,

    @field:Email(regexp = ".+@.+\\..+")
    var email: String? = null,

    @field:Pattern(
        regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}",
        message = "Must be a valid phone number"
    )
    var phoneNumber: String? = null
) {
    override fun toString(): String {
        return "Person(id=$id, name='$name', email=$email, phoneNumber=$phoneNumber)"
    }
}
