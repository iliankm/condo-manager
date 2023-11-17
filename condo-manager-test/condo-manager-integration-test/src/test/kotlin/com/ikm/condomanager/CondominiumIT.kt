package com.ikm.condomanager

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.Role.CONDOMINIUM_MANAGE
import com.ikm.condomanager.domain.Role.CONDOMINIUM_READ
import com.ikm.condomanager.domain.Role.CONDO_MANAGER_USER
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_FORBIDDEN
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.apache.http.HttpStatus.SC_OK
import org.apache.http.HttpStatus.SC_PRECONDITION_FAILED
import org.apache.http.HttpStatus.SC_UNAUTHORIZED
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import kotlin.test.Test

/**
 * Integration test for Condominium resource.
 */
class CondominiumIT : BaseIntegrationTest() {
    companion object {
        private const val CONDOMINIUMS_URI = "api/v1/condominiums"

        private val condoManagerUser = keycloak.createUser(CONDO_MANAGER_USER)
        private val condominiumManageUser = keycloak.createUser(CONDOMINIUM_READ, CONDOMINIUM_MANAGE)
        private val condominiumReadUser = keycloak.createUser(CONDOMINIUM_READ)

        private fun whenCreateCondominium(
            user: KeycloakUser?,
            createDTO: CondominiumDTO = CondominiumDTO(
                id = null,
                address = CondominiumAddressDTO(
                    city = randomAlphabetic(20),
                    street = randomAlphabetic(20),
                    houseNumber = 1,
                    location = null
                )
            )
        ) =
            Given {
                contentType(JSON)
                oauth2(user)
                body(createDTO)
            } When {
                post(CONDOMINIUMS_URI)
            }

        fun whenGetCondominium(user: KeycloakUser?, id: String) =
            Given {
                oauth2(user)
                pathParam("id", id)
            } When {
                get("$CONDOMINIUMS_URI/{id}")
            }

        fun whenUpdateCondominium(user: KeycloakUser?, id: CondominiumId, condominium: CondominiumDTO) =
            Given {
                contentType(JSON)
                oauth2(user)
                pathParam("id", id.id)
                header(HttpHeaders.IF_MATCH, id.version)
                body(condominium)
            } When {
                put("$CONDOMINIUMS_URI/{id}")
            }

        fun whenDeleteCondominium(user: KeycloakUser?, id: CondominiumId) =
            Given {
                oauth2(user)
                pathParam("id", id.id)
                header(HttpHeaders.IF_MATCH, id.version)
            } When {
                delete("$CONDOMINIUMS_URI/{id}")
            }

        @JvmStatic
        fun createCondominiumTestParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(keycloak.dummyUser, SC_FORBIDDEN),
                Arguments.of(condoManagerUser, SC_FORBIDDEN),
                Arguments.of(condominiumManageUser, SC_CREATED),
            )

        @JvmStatic
        fun getCondominiumTestParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(keycloak.dummyUser, SC_FORBIDDEN),
                Arguments.of(condoManagerUser, SC_FORBIDDEN),
                Arguments.of(condominiumReadUser, SC_OK),
            )

        @JvmStatic
        fun updateSecurityTestParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(keycloak.dummyUser, SC_FORBIDDEN),
                Arguments.of(condoManagerUser, SC_FORBIDDEN),
                Arguments.of(condominiumManageUser, SC_OK),
            )
    }

    @ParameterizedTest
    @MethodSource("createCondominiumTestParameters")
    fun `create Condominium test`(user: KeycloakUser?, statusCode: Int) {
        whenCreateCondominium(user) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `given not valid payload, when create Condominium, should return 400`() {
        whenCreateCondominium(
            condominiumManageUser,
            CondominiumDTO(
                id = null,
                address = CondominiumAddressDTO(
                    city = "",
                    street = "",
                    houseNumber = 1,
                    location = null
                )
            )
        ) Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @ParameterizedTest
    @MethodSource("getCondominiumTestParameters")
    fun `get Condominium by id test`(user: KeycloakUser?, statusCode: Int) {
        val id = whenCreateCondominium(condominiumManageUser) Extract {
            path<String>("id.id")
        }

        whenGetCondominium(user, id) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try getting not existing Condominium, should return 404`() {
        whenGetCondominium(condominiumReadUser, UUID.randomUUID().toString()) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @ParameterizedTest
    @MethodSource("updateSecurityTestParameters")
    fun `update existing Condominium test`(user: KeycloakUser?, statusCode: Int) {
        val condominiumDTO = whenCreateCondominium(condominiumManageUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = user,
            id = condominiumDTO.id!!,
            condominium = condominiumDTO.copy(
                address = CondominiumAddressDTO(
                    city = randomAlphabetic(20),
                    street = randomAlphabetic(20),
                    houseNumber = 1,
                    location = null
                )
            )
        ) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `given not valid payload, when update Condominium, should return 400`() {
        val condominiumDTO = whenCreateCondominium(condominiumManageUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = condominiumManageUser,
            id = condominiumDTO.id!!,
            condominium = condominiumDTO.copy(
                address = condominiumDTO.address.copy(city = "")
            )
        ) Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @Test
    fun `when try updating not existing Condominium, should return 404`() {
        whenUpdateCondominium(
            user = condominiumManageUser,
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            condominium = CondominiumDTO(
                id = CondominiumId(UUID.randomUUID().toString(), 1),
                address = CondominiumAddressDTO(
                    city = randomAlphabetic(20),
                    street = randomAlphabetic(20),
                    houseNumber = 1,
                    location = null
                )
            )
        ) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try updating a Condominium with wrong version, should return 412`() {
        val condominiumDTO = whenCreateCondominium(condominiumManageUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = condominiumManageUser,
            id = CondominiumId(condominiumDTO.id!!.id, 100),
            condominium = condominiumDTO
        ) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }

    @ParameterizedTest
    @MethodSource("updateSecurityTestParameters")
    fun `delete Condominium test`(user: KeycloakUser?, statusCode: Int) {
        val condominiumDTO = whenCreateCondominium(condominiumManageUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenDeleteCondominium(user, condominiumDTO.id!!) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try deleting not existing Condominium, should return 404`() {
        whenDeleteCondominium(condominiumManageUser, CondominiumId(UUID.randomUUID().toString(), 1)) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try deleting a Condominium with wrong version, should return 412`() {
        val condominiumDTO = whenCreateCondominium(condominiumManageUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenDeleteCondominium(condominiumManageUser, CondominiumId(condominiumDTO.id!!.id, 100)) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }
}
