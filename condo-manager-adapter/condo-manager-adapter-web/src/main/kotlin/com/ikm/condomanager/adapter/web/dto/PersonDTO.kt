package com.ikm.condomanager.adapter.web.dto

import com.ikm.condomanager.domain.PersonId

/**
 * DTO for person resource.
 */
data class PersonDTO(
    val name: String,
    val id: PersonId? = null,
    val email: String? = null,
    val phoneNumber: String? = null
)
