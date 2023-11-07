package com.ikm.condomanager

import com.ikm.condomanager.adapter.web.dto.PersonDTO
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.domain.Role
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
 * Integration test for Person resource.
 */
class PersonIT : BaseIntegrationTest() {
    companion object {
        private const val PERSONS_URI = "api/v1/persons"

        private val condoManagerUser = keycloak.createUser(Role.CONDO_MANAGER_USER)

        private fun whenCreatePerson(user: KeycloakUser? = condoManagerUser, name: String = randomAlphabetic(20)) =
            Given {
                contentType(JSON)
                oauth2(user)
                body(
                    PersonDTO(
                        name = name
                    )
                )
            } When {
                post(PERSONS_URI)
            }

        private fun whenGetPerson(user: KeycloakUser?, id: String) =
            Given {
                oauth2(user)
                pathParam("id", id)
            } When {
                get("$PERSONS_URI/{id}")
            }

        private fun whenUpdatePerson(user: KeycloakUser?, id: PersonId, person: PersonDTO) =
            Given {
                contentType(JSON)
                oauth2(user)
                pathParam("id", id.id)
                header(HttpHeaders.IF_MATCH, id.version)
                body(person)
            } When {
                put("$PERSONS_URI/{id}")
            }

        private fun whenDeletePerson(user: KeycloakUser?, id: PersonId) =
            Given {
                oauth2(user)
                pathParam("id", id.id)
                header(HttpHeaders.IF_MATCH, id.version)
            } When {
                delete("$PERSONS_URI/{id}")
            }

        @JvmStatic
        fun personSecurityTestParameters() =
            listOf(
                Arguments.of(null, SC_UNAUTHORIZED),
                Arguments.of(keycloak.dummyUser, SC_FORBIDDEN)
            )
    }

    @Test
    fun `should create Person`() {
        whenCreatePerson() Then {
            statusCode(SC_CREATED)
        }
    }

    @Test
    fun `given not valid payload, when create Person, should return 400`() {
        whenCreatePerson(name = "") Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @ParameterizedTest
    @MethodSource("personSecurityTestParameters")
    fun `create Person security test`(user: KeycloakUser?, statusCode: Int) {
        whenCreatePerson(user = user) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `should get Person by id`() {
        val id = whenCreatePerson() Extract {
            path<String>("id.id")
        }

        whenGetPerson(condoManagerUser, id) Then {
            statusCode(SC_OK)
            body("id.id", equalTo(id))
        }
    }

    @ParameterizedTest
    @MethodSource("personSecurityTestParameters")
    fun `get Person security test`(user: KeycloakUser?, statusCode: Int) {
        whenGetPerson(user, UUID.randomUUID().toString()) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try getting not existing Person, should return 404`() {
        whenGetPerson(condoManagerUser, UUID.randomUUID().toString()) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `should update existing Person`() {
        val person = whenCreatePerson() Extract {
            `as`(PersonDTO::class.java)
        }

        whenUpdatePerson(condoManagerUser, person.id!!, person.copy(name = "John Doe")) Then {
            statusCode(SC_OK)
            body("name", equalTo("John Doe"))
        }
    }

    @Test
    fun `given not valid payload, when update Person, should return 400`() {
        val person = whenCreatePerson() Extract {
            `as`(PersonDTO::class.java)
        }

        whenUpdatePerson(condoManagerUser, person.id!!, person.copy(name = "")) Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @ParameterizedTest
    @MethodSource("personSecurityTestParameters")
    fun `update Person security test`(user: KeycloakUser?, statusCode: Int) {
        whenUpdatePerson(
            user = user,
            id = PersonId(UUID.randomUUID().toString(), 1),
            person = PersonDTO(name = "John Doe")
        ) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try updating not existing Person, should return 404`() {
        whenUpdatePerson(
            user = condoManagerUser,
            id = PersonId(UUID.randomUUID().toString(), 1),
            person = PersonDTO(name = "John Doe")
        ) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try updating a Person with wrong version, should return 412`() {
        val person = whenCreatePerson() Extract {
            `as`(PersonDTO::class.java)
        }

        whenUpdatePerson(
            user = condoManagerUser,
            id = PersonId(person.id!!.id, 100),
            person.copy(name = "John Doe")
        ) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }

    @Test
    fun `should delete Person`() {
        val person = whenCreatePerson() Extract {
            `as`(PersonDTO::class.java)
        }

        whenDeletePerson(condoManagerUser, person.id!!) Then {
            statusCode(SC_OK)
        }
    }

    @ParameterizedTest
    @MethodSource("personSecurityTestParameters")
    fun `delete Person security test`(user: KeycloakUser?, statusCode: Int) {
        whenDeletePerson(user, PersonId(UUID.randomUUID().toString(), 1)) Then {
            statusCode(statusCode)
        }
    }

    @Test
    fun `when try deleting not existing Person, should return 404`() {
        whenDeletePerson(condoManagerUser, PersonId(UUID.randomUUID().toString(), 1)) Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try deleting a Person with wrong version, should return 412`() {
        val person = whenCreatePerson() Extract {
            `as`(PersonDTO::class.java)
        }

        whenDeletePerson(condoManagerUser, PersonId(person.id!!.id, 100)) Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }
}
