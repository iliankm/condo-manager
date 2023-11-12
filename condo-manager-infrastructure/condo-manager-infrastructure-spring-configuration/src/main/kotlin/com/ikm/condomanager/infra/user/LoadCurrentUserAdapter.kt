package com.ikm.condomanager.infra.user

import com.ikm.condomanager.domain.AuthenticatedUser
import com.ikm.condomanager.domain.Role
import com.ikm.condomanager.port.user.LoadCurrentUserPort
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

/**
 * Adapter implementation of [LoadCurrentUserPort].
 */
@Component
class LoadCurrentUserAdapter : LoadCurrentUserPort {
    override fun load(): AuthenticatedUser? =
        SecurityContextHolder.getContext().authentication.let { auth ->
            when (auth) {
                is JwtAuthenticationToken -> AuthenticatedUser(
                    username = auth.name!!,
                    email = auth.token.getClaimAsString("email"),
                    firstName = auth.token.getClaimAsString("given_name"),
                    lastName = auth.token.getClaimAsString("family_name"),
                    roles = auth.authorities
                        .map { it.authority.removePrefix("ROLE_") }
                        .filter { Role.hasValue(it) }
                        .map { Role.fromValue(it) }
                        .toSet()
                )
                else -> null
            }
        }
}
