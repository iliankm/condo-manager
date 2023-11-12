package com.ikm.condomanager.infra.user

import com.ikm.condomanager.domain.Role
import com.ikm.condomanager.port.user.LoadCurrentUserPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.Test

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LoadCurrentUserAdapter::class])
class LoadCurrentUserAdapterTest {
    @Autowired
    lateinit var loadCurrentUserPort: LoadCurrentUserPort

    @Test
    fun `given no authentication, should return null`() {
        // given
        SecurityContextHolder.clearContext()
        // when
        val user = loadCurrentUserPort.load()
        // then
        assertNull(user)
    }

    @Test
    fun `given JwtAuthenticationToken, should return AuthenticatedUser`() {
        // given
        val auth = JwtAuthenticationToken(
            Jwt.withTokenValue("123")
                .header("alg", "RS256")
                .header("typ", "JWT")
                .claim("email", "john.doe@domain.com")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build(),
            listOf(SimpleGrantedAuthority("ROLE_test"), SimpleGrantedAuthority("ROLE_condo_manager_user")),
            "john_doe"
        )
        SecurityContextHolder.setContext(SecurityContextImpl(auth))
        // when
        val user = loadCurrentUserPort.load()!!
        // then
        assertEquals("john_doe", user.username)
        assertEquals("john.doe@domain.com", user.email)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals(setOf(Role.CONDO_MANAGER_USER), user.roles)
    }
}
