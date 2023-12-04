package com.ikm.condomanager.infra.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ikm.condomanager.domain.AuthenticatedUser
import com.ikm.condomanager.domain.Role.CONDO_MANAGER_USER
import com.ikm.condomanager.infra.user.LoadCurrentUserAdapter
import com.ikm.condomanager.port.user.LoadCurrentUserPort
import io.mockk.every
import io.mockk.mockk
import jakarta.annotation.security.RolesAllowed
import org.hamcrest.CoreMatchers.hasItems
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset.defaultCharset
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.Base64
import java.util.UUID
import kotlin.test.assertEquals

/**
 * Spring WebMvc test for [SecurityConfiguration].
 */
@WebMvcTest
@Import(SecurityConfiguration::class)
@ContextConfiguration(classes = [TestComponent::class, TestController::class, LoadCurrentUserAdapter::class])
@ActiveProfiles("test-infra-spring")
class SecurityConfigurationTest {
    @Autowired
    private lateinit var mvc: MockMvc

    companion object {
        private val publicKey: RSAPublicKey =
            ClassPathResource("public_key.pem").getContentAsString(defaultCharset()).toPublicKey()

        private val privateKey: RSAPrivateKey =
            ClassPathResource("private_key_pkcs8.pem").getContentAsString(defaultCharset()).toPrivateKey()

        private fun String.toPublicKey() =
            let {
                Base64.getDecoder().decode(it)
            }.let {
                X509EncodedKeySpec(it)
            }.let {
                KeyFactory.getInstance("RSA").generatePublic(it) as RSAPublicKey
            }

        private fun String.toPrivateKey() =
            let {
                Base64.getDecoder().decode(it)
            }.let {
                PKCS8EncodedKeySpec(it)
            }.let {
                KeyFactory.getInstance("RSA").generatePrivate(it) as RSAPrivateKey
            }

        @JvmStatic
        fun authenticationUsernameParameters() =
            listOf(
                Arguments.of(mockk<JwtAuthenticationToken>().apply { every { name } returns "my-user" }, "my-user"),
                Arguments.of(TestingAuthenticationToken(User("my-user2", "", emptySet()), ""), "my-user2"),
                Arguments.of(TestingAuthenticationToken("", ""), null)
            )
    }

    @Test
    fun `should return 401 Unauthorized`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/hello")
        )
        // then
        result.andExpect(status().isUnauthorized)
    }

    @ParameterizedTest
    @CsvSource(
        "invalid_role",
        "condo_manager_user",
        "TEST1",
        "TEST2",
        "TEST3",
        "TEST1;TEST2;TEST3",
        "condo_manager_user;TEST2;TEST3",
        "condo_manager_user;TEST1;TEST3",
        "condo_manager_user;TEST1;TEST2"
    )
    fun `should return 403 Forbidden`(roles: String) {
        // given
        val jwt = JWT.create()
            .withNotBefore(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withSubject(UUID.randomUUID().toString())
            .withArrayClaim("roles", roles.split(";").toTypedArray())
            .sign(Algorithm.RSA256(publicKey, privateKey))
        // when
        val result = mvc.perform(
            get("/api/v1/test/hello").header(AUTHORIZATION, "Bearer $jwt")
        )
        // then
        result.andExpect(status().isForbidden)
    }

    @Test
    fun `should return 200 OK`() {
        // given
        val jwt = JWT.create()
            .withNotBefore(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withSubject(UUID.randomUUID().toString())
            .withArrayClaim("roles", arrayOf(CONDO_MANAGER_USER.value, "TEST1", "TEST2", "TEST3"))
            .sign(Algorithm.RSA256(publicKey, privateKey))
        // when
        val result = mvc.perform(
            get("/api/v1/test/hello").header(AUTHORIZATION, "Bearer $jwt")
        )
        // then
        result.andExpect(status().isOk)
    }

    @Test
    fun `when OPTIONS, should return 200 OK`() {
        // when
        val result = mvc.perform(
            options("/api/v1/test/hello")
        )
        // then
        result.andExpect(status().isOk)
    }

    @Test
    fun `should get current user`() {
        // given
        val jwt = JWT.create()
            .withNotBefore(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withSubject(UUID.randomUUID().toString())
            .withClaim("preferred_username", "user")
            .withClaim("email", "user@domain.com")
            .withClaim("given_name", "John")
            .withClaim("family_name", "Doe")
            .withArrayClaim("roles", arrayOf(CONDO_MANAGER_USER.value))
            .sign(Algorithm.RSA256(publicKey, privateKey))
        // when
        val result = mvc.perform(
            get("/api/v1/test/user").header(AUTHORIZATION, "Bearer $jwt")
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(jsonPath("$.username").value("user"))
        result.andExpect(jsonPath("$.email").value("user@domain.com"))
        result.andExpect(jsonPath("$.firstName").value("John"))
        result.andExpect(jsonPath("$.lastName").value("Doe"))
        result.andExpect(jsonPath("$.roles", hasItems(CONDO_MANAGER_USER.name)))
    }

    @Test
    fun `should get current user from MDC context`() {
        // given
        val jwt = JWT.create()
            .withNotBefore(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withSubject(UUID.randomUUID().toString())
            .withClaim("preferred_username", "user")
            .withArrayClaim("roles", arrayOf(CONDO_MANAGER_USER.value))
            .sign(Algorithm.RSA256(publicKey, privateKey))
        // when
        val result = mvc.perform(
            get("/api/v1/test/user-in-mdc").header(AUTHORIZATION, "Bearer $jwt")
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(content().string("user"))
    }

    @ParameterizedTest
    @MethodSource("authenticationUsernameParameters")
    fun `should return username`(authentication: Authentication, expectedUsername: String?) {
        assertEquals(expectedUsername, authentication.username)
    }
}

@RestController
@RequestMapping("api/v1/test")
class TestController(
    val testComponent: TestComponent,
    val loadCurrentUserPort: LoadCurrentUserPort
) {
    @GetMapping("{value}")
    fun echo(@PathVariable("value") value: String): String {
        testComponent.test1()
        testComponent.test2()
        testComponent.test3()
        return value
    }

    @GetMapping("user")
    fun getUser(): AuthenticatedUser? =
        loadCurrentUserPort.load()

    @GetMapping("user-in-mdc")
    fun getUserInMdc(): String =
        MDC.get("user")
}

@Component
class TestComponent {
    @RolesAllowed("TEST1")
    fun test1() = Unit

    @Secured("ROLE_TEST2")
    fun test2() = Unit

    @PreAuthorize("hasRole('TEST3')")
    fun test3() = Unit
}
