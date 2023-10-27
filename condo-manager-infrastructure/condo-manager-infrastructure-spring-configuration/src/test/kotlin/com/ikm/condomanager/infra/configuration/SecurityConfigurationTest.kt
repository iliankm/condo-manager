package com.ikm.condomanager.infra.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ikm.condomanager.domain.Role
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options
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

/**
 * Spring WebMvc test for [SecurityConfiguration].
 */
@WebMvcTest
@Import(SecurityConfiguration::class)
@ContextConfiguration(classes = [TestController::class])
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

    @Test
    fun `should return 403 Forbidden`() {
        // given
        val jwt = JWT.create()
            .withNotBefore(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(60))
            .withSubject(UUID.randomUUID().toString())
            .withArrayClaim("roles", arrayOf("invalid_role"))
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
            .withArrayClaim("roles", arrayOf(Role.CONDO_MANAGER_USER.value))
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
}

@RestController
@RequestMapping("api/v1/test")
class TestController {
    @GetMapping("{value}")
    fun echo(@PathVariable("value") value: String) =
        value
}
