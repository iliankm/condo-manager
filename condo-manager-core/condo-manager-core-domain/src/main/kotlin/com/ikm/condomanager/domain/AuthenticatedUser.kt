package com.ikm.condomanager.domain

/**
 * Representation of an authenticated user with assigned roles.
 */
data class AuthenticatedUser(
    val username: String,
    val email: String?,
    val firstName: String?,
    val lastName: String?,
    val roles: Set<Role>
)
