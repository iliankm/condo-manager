package com.ikm.condomanager.port.user

import com.ikm.condomanager.domain.AuthenticatedUser

/**
 * Out port for loading the current [AuthenticatedUser].
 */
interface LoadCurrentUserPort {
    /**
     * Loads the current authenticated user according to the current security context.
     *
     * @return current logged user or null if no authentication is present
     */
    fun load(): AuthenticatedUser?
}
