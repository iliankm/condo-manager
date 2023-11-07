package com.ikm.condomanager

import com.ikm.condomanager.domain.Role
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_NO_CONTENT
import org.apache.http.HttpStatus.SC_OK
import java.time.Duration
import java.time.Instant
import java.util.UUID
import kotlin.reflect.KProperty

/**
 * Utility class for Keycloak.
 */
class Keycloak(
    private val host: String
) {

    private val adminAccessToken: String by timeout(Duration.ofSeconds(58)) {
        issueKeycloakAdminAccessToken()
    }

    private val realmRoles: Map<String, String> by lazy { loadRealmRoles() }

    internal val dummyUser = KeycloakUser(
        keycloak = this,
        id = "",
        username = "dummy",
        password = "dummy",
        roles = emptyList()
    )

    companion object {
        private const val KEYCLOAK_REALM = "condo-manager"
        private const val KEYCLOAK_CLIENT = "condo-manager"
        private const val KEYCLOAK_ADMIN_CLIENT = "admin-cli"
        private const val KEYCLOAK_ADMIN_USER = "admin"
        private const val KEYCLOAK_ADMIN_PASSWORD = "admin"
    }

    /**
     * Creates user with given roles, put it in the state.
     *
     * @param roles
     * @return the created [KeycloakUser]
     */
    fun createUser(vararg roles: Role): KeycloakUser {
        val username = UUID.randomUUID().toString()
        val password = UUID.randomUUID().toString()
        val userId = Given {
            contentType(ContentType.JSON)
            oauth2(adminAccessToken)
            body(
                mapOf(
                    "username" to username,
                    "credentials" to listOf(
                        mapOf(
                            "type" to "password",
                            "value" to password,
                            "temporary" to false
                        )
                    ),
                    "enabled" to true,
                    "realmRoles" to roles.map { it.value }
                )
            )
        } When {
            post("$host/admin/realms/$KEYCLOAK_REALM/users")
        } Then {
            statusCode(SC_CREATED)
        } Extract {
            header(HttpHeaders.LOCATION).split("/").last()
        }

        mapRolesToUser(userId, *roles)

        return KeycloakUser(
            this,
            id = userId,
            username = username,
            password = password,
            roles = roles.asList()
        )
    }

    /**
     * Issues access token for a user.
     *
     * @param user registered Keycloak user
     * @return the issued access token
     */
    fun issueAccessToken(user: KeycloakUser): String =
        Given {
            contentType(ContentType.URLENC)
            basicAuth(KEYCLOAK_CLIENT, "")
            formParam("username", user.username)
            formParam("password", user.password)
            formParam("grant_type", "password")
            formParam("scope", "openid")
        } When {
            post("$host/realms/$KEYCLOAK_REALM/protocol/openid-connect/token")
        } Then {
            statusCode(SC_OK)
        } Extract {
            body().jsonPath().getString("access_token")
        }

    private fun issueKeycloakAdminAccessToken(): String =
        Given {
            contentType(ContentType.URLENC)
            basicAuth(KEYCLOAK_ADMIN_CLIENT, "")
            formParam("username", KEYCLOAK_ADMIN_USER)
            formParam("password", KEYCLOAK_ADMIN_PASSWORD)
            formParam("grant_type", "password")
        } When {
            post("$host/realms/master/protocol/openid-connect/token")
        } Then {
            statusCode(SC_OK)
        } Extract {
            body().jsonPath().getString("access_token")
        }

    private fun loadRealmRoles(): Map<String, String> =
        (
            Given {
                contentType(ContentType.JSON)
                oauth2(adminAccessToken)
            } When {
                get("$host/admin/realms/$KEYCLOAK_REALM/roles")
            } Then {
                statusCode(SC_OK)
            } Extract {
                `as`(List::class.java)
            }
            ).associateBy(
            { (it as Map<*, *>)["name"] as String },
            { (it as Map<*, *>)["id"] as String }
        )

    private fun mapRolesToUser(userId: String, vararg roles: Role) {
        roles.filter { !realmRoles.containsKey(it.value) }.apply {
            check(isEmpty()) { "Role(s): $this don't exists in keycloak" }
        }

        Given {
            contentType(ContentType.JSON)
            oauth2(adminAccessToken)
            pathParam("userId", userId)
            body(
                roles.map {
                    mapOf(
                        "id" to realmRoles[it.value]!!,
                        "name" to it.value,
                        "composite" to false,
                        "clientRole" to false
                    )
                }
            )
        } When {
            post("$host/admin/realms/$KEYCLOAK_REALM/users/{userId}/role-mappings/realm")
        } Then {
            statusCode(SC_NO_CONTENT)
        }
    }

    override fun toString(): String {
        return "Keycloak(host='$host')"
    }
}

data class KeycloakUser(
    val keycloak: Keycloak,
    val id: String,
    val username: String,
    val password: String,
    val roles: List<Role>
) {
    val token by timeout(Duration.ofSeconds(298)) {
        keycloak.issueAccessToken(this)
    }

    override fun toString(): String {
        return "KeycloakUser(keycloak=$keycloak, username='$username', roles=$roles)"
    }
}

/**
 * Property delegate for timeout properties.
 */
class TimeoutPropertyDelegate<T>(
    private val timeout: Duration,
    private val initializer: () -> T
) {
    @Volatile
    private var initializedAt: Instant = Instant.MIN

    @Volatile
    private var value: T? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (shouldInitialize()) {
            synchronized(this) {
                if (shouldInitialize()) {
                    value = initializer()
                    initializedAt = Instant.now()
                }
            }
        }
        return value!!
    }

    private fun shouldInitialize(): Boolean =
        value == null || Duration.between(initializedAt, Instant.now()).seconds >= timeout.seconds
}

fun <T> timeout(timeout: Duration, initializer: () -> T) =
    TimeoutPropertyDelegate(timeout, initializer)
