package com.ikm.condomanager.infra.configuration

import com.ikm.condomanager.infra.controller.EchoController
import com.ikm.condomanager.infra.controller.TestComponent
import com.ikm.condomanager.infra.user.LoadCurrentUserAdapter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Spring WebMvc test for [SecurityConfiguration].
 */
@WebMvcTest
@Import(SecurityConfiguration::class)
@ContextConfiguration(classes = [TestComponent::class, EchoController::class, LoadCurrentUserAdapter::class])
@ActiveProfiles("test-infra-spring")
class SecurityConfigurationTest {
    @Autowired
    private lateinit var securityFilterChain: SecurityFilterChain

    @Autowired
    private lateinit var jwtGrantedAuthoritiesConverter: JwtGrantedAuthoritiesConverter

    @Autowired
    private lateinit var jwtAuthenticationConverter: JwtAuthenticationConverter

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    companion object {
        @JvmStatic
        fun authenticationUsernameParameters() =
            listOf(
                Arguments.of(mockk<JwtAuthenticationToken>().apply { every { name } returns "my-user" }, "my-user"),
                Arguments.of(TestingAuthenticationToken(User("my-user2", "", emptySet()), ""), "my-user2"),
                Arguments.of(TestingAuthenticationToken("", ""), null)
            )
    }

    @Test
    fun `should inject SecurityFilterChain`() {
        assertNotNull(securityFilterChain)
    }

    @Test
    fun `should inject JwtGrantedAuthoritiesConverter`() {
        assertNotNull(jwtGrantedAuthoritiesConverter)
    }

    @Test
    fun `should inject JwtAuthenticationConverter`() {
        assertNotNull(jwtAuthenticationConverter)
    }

    @Test
    fun `should inject UserDetailsService`() {
        assertNotNull(userDetailsService)
    }

    @ParameterizedTest
    @MethodSource("authenticationUsernameParameters")
    fun `should return username`(authentication: Authentication, expectedUsername: String?) {
        assertEquals(expectedUsername, authentication.username)
    }
}
