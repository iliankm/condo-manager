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
 *
 * @property id the id of the person. If null, it is still not persisted and is created only in the memory.
 * @property name the name of the person. Must be not blank and max size is 70.
 * @property email the email of the person. Must be a valid email.
 * @property phoneNumber the phone number of the person. Must be a valid phone number in the following formats:
 * 0888111222
 * 0888-111-222
 * 0888 111 222
 * +359894991153
 * +359 894 991 153
 */
class Person(
    @field:Valid
    val id: PersonId? = null,

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
