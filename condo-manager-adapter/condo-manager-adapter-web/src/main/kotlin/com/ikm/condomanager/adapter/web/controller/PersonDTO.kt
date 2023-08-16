package com.ikm.condomanager.adapter.web.controller

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
