package com.ikm.condomanager

import com.ikm.condomanager.domain.Role
import com.ikm.condomanager.domain.Role.CONDOMINIUM_READ
import com.ikm.condomanager.domain.Role.CONDO_MANAGER_USER
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus.SC_FORBIDDEN
import org.apache.http.HttpStatus.SC_OK
import org.apache.http.HttpStatus.SC_UNAUTHORIZED
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

/**
 * Integration test for the Spring security configuration.
 */
class SecurityConfigurationIT : BaseIntegrationTest() {

    companion object {
        private const val URI = "api/v1/test"

        private val simpleUser = keycloak.createUser(CONDO_MANAGER_USER)
        private val condominiumReadUser = keycloak.createUser(CONDOMINIUM_READ)

        @JvmStatic
        fun pingParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(simpleUser, SC_FORBIDDEN),
                Arguments.of(condominiumReadUser, SC_OK)
            )

        @JvmStatic
        fun getUserParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED, null, emptyList<Role>()),
                Arguments.of(simpleUser, SC_OK, simpleUser.username, listOf(CONDO_MANAGER_USER)),
                Arguments.of(
                    condominiumReadUser,
                    SC_OK,
                    condominiumReadUser.username,
                    listOf(CONDO_MANAGER_USER, CONDOMINIUM_READ)
                )
            )
    }

    @Test
    fun `given no auth, when OPTIONS, should return 200`() {
        Given {
            pathParam("value", "ping")
        } When {
            options("$URI/{value}")
        } Then {
            statusCode(SC_OK)
        }
    }

    @ParameterizedTest
    @MethodSource("pingParameters")
    fun `given a user, should ping`(user: KeycloakUser?, statusCode: Int) {
        Given {
            oauth2(user)
            pathParam("value", "ping")
        } When {
            get("$URI/{value}")
        } Then {
            statusCode(statusCode)
        }
    }

    @ParameterizedTest
    @MethodSource("getUserParameters")
    fun `should get current user`(user: KeycloakUser?, statusCode: Int, username: String?, roles: List<Role>) {
        Given {
            oauth2(user)
        } When {
            get("$URI/user")
        } Then {
            statusCode(statusCode)
            if (username != null) {
                body("username", equalTo(username))
                roles.forEach { body("roles", hasItem(it.name)) }
                body("roles", hasSize<String>(roles.size))
            }
        }
    }

    @ParameterizedTest
    @MethodSource("getUserParameters")
    fun `should get current user from MDC context`(
        user: KeycloakUser?,
        statusCode: Int,
        username: String?
    ) {
        Given {
            oauth2(user)
        } When {
            get("$URI/user-in-mdc")
        } Then {
            statusCode(statusCode)
            if (username != null) {
                body(equalTo(username))
            }
        }
    }

    @ParameterizedTest
    @CsvSource("health", "info")
    fun `given no auth, should get actuator health and info endpoints`(uri: String) {
        Given {
            this
        } When {
            get("actuator/$uri")
        } Then {
            statusCode(SC_OK)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "beans",
        "caches",
        "conditions",
        "configprops",
        "env",
        "flyway",
        "loggers",
        "metrics",
        "mappings",
        "scheduledtasks",
        "threaddump",
        "heapdump",
        "logfile"
    )
    fun `given no auth, should not get protected actuator endpoints`(uri: String) {
        Given {
            this
        } When {
            get("actuator/$uri")
        } Then {
            statusCode(SC_UNAUTHORIZED)
        }
    }

    @ParameterizedTest
    @CsvSource(
        "beans",
        "caches",
        "conditions",
        "configprops",
        "env",
        "flyway",
        "loggers",
        "metrics",
        "mappings",
        "scheduledtasks",
        "threaddump",
        "heapdump",
        "logfile"
    )
    fun `given basic auth for monitoring user, should get protected actuator endpoints`(uri: String) {
        Given {
            basicAuth("admin", "admin")
        } When {
            get("actuator/$uri")
        } Then {
            statusCode(SC_OK)
        }
    }
}
