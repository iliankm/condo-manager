package com.ikm.condomanager

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumCreateDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumUpdateDTO
import com.ikm.condomanager.domain.CondominiumId
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
import org.hamcrest.Matchers.equalTo
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

        private fun whenCreateCondominium(
            user: KeycloakUser?,
            createDTO: CondominiumCreateDTO = CondominiumCreateDTO(
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

        fun whenUpdateCondominium(user: KeycloakUser?, id: CondominiumId, updateData: CondominiumUpdateDTO) =
            Given {
                contentType(JSON)
                oauth2(user)
                pathParam("id", id.id)
                header(HttpHeaders.IF_MATCH, id.version)
                body(updateData)
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
        fun condominiumSecurityTestParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(keycloak.dummyUser, SC_FORBIDDEN)
            )
    }

    @Test
    fun `should create Condominium`() {
        whenCreateCondominium(condoManagerUser) Then {
            statusCode(SC_CREATED)
        }
    }

    @Test
    fun `given not valid payload, when create Condominium, should return 400`() {
        whenCreateCondominium(
            condoManagerUser,
            CondominiumCreateDTO(
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
    @MethodSource("condominiumSecurityTestParameters")
    fun `create Condominium security test`(user: KeycloakUser?, statusCode: Int) {
        whenCreateCondominium(user) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `should get Condominium by id`() {
        val id = whenCreateCondominium(condoManagerUser) Extract {
            path<String>("id.id")
        }

        whenGetCondominium(condoManagerUser, id) Then {
            statusCode(SC_OK)
            body("id.id", equalTo(id))
        }
    }

    @ParameterizedTest
    @MethodSource("condominiumSecurityTestParameters")
    fun `get Condominium security test`(user: KeycloakUser?, statusCode: Int) {
        whenGetCondominium(user, UUID.randomUUID().toString()) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try getting not existing Condominium, should return 404`() {
        whenGetCondominium(condoManagerUser, UUID.randomUUID().toString()) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `should update existing Condominium`() {
        val condominiumDTO = whenCreateCondominium(condoManagerUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = condoManagerUser,
            id = condominiumDTO.id,
            updateData = CondominiumUpdateDTO(
                address = condominiumDTO.address.copy(city = "Some City")
            )
        ) Then {
            statusCode(SC_OK)
            body("address.city", equalTo("Some City"))
        }
    }

    @Test
    fun `given not valid payload, when update Condominium, should return 400`() {
        val condominiumDTO = whenCreateCondominium(condoManagerUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = condoManagerUser,
            id = condominiumDTO.id,
            updateData = CondominiumUpdateDTO(
                address = condominiumDTO.address.copy(city = "")
            )
        ) Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @ParameterizedTest
    @MethodSource("condominiumSecurityTestParameters")
    fun `update Condominium security test`(user: KeycloakUser?, statusCode: Int) {
        whenUpdateCondominium(
            user = user,
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            updateData = CondominiumUpdateDTO(
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
    fun `when try updating not existing Condominium, should return 404`() {
        whenUpdateCondominium(
            user = condoManagerUser,
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            updateData = CondominiumUpdateDTO(
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
        val condominiumDTO = whenCreateCondominium(condoManagerUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenUpdateCondominium(
            user = condoManagerUser,
            id = CondominiumId(condominiumDTO.id.id, 100),
            updateData = CondominiumUpdateDTO(
                address = condominiumDTO.address.copy()
            )
        ) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }

    @Test
    fun `should delete Condominium`() {
        val condominiumDTO = whenCreateCondominium(condoManagerUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenDeleteCondominium(condoManagerUser, condominiumDTO.id) Then {
            statusCode(SC_OK)
        }
    }

    @ParameterizedTest
    @MethodSource("condominiumSecurityTestParameters")
    fun `delete Condominium security test`(user: KeycloakUser?, statusCode: Int) {
        whenDeleteCondominium(user, CondominiumId(UUID.randomUUID().toString(), 1)) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try deleting not existing Condominium, should return 404`() {
        whenDeleteCondominium(condoManagerUser, CondominiumId(UUID.randomUUID().toString(), 1)) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try deleting a Condominium with wrong version, should return 412`() {
        val condominiumDTO = whenCreateCondominium(condoManagerUser) Extract {
            `as`(CondominiumDTO::class.java)
        }

        whenDeleteCondominium(condoManagerUser, CondominiumId(condominiumDTO.id.id, 100)) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }
}
