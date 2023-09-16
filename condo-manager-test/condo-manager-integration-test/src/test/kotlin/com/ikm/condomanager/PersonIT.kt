package com.ikm.condomanager

import com.ikm.condomanager.adapter.web.dto.PersonDTO
import io.restassured.http.ContentType.JSON
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.apache.http.HttpStatus.SC_OK
import org.apache.http.HttpStatus.SC_PRECONDITION_FAILED
import org.hamcrest.Matchers.equalTo
import java.util.UUID
import kotlin.test.Test

/**
 * Integration test for Person resource.
 */
class PersonIT : BaseIntegrationTest() {
    companion object {
        private const val PERSONS_URI = "api/v1/persons"
        private fun givenAPerson(name: String = randomAlphabetic(20)) =
            Given {
                contentType(JSON)
                body(
                    PersonDTO(
                        name = name
                    )
                )
            } When {
                post(PERSONS_URI)
            }
    }

    @Test
    fun `should create Person`() {
        givenAPerson() Then {
            statusCode(SC_CREATED)
        }
    }

    @Test
    fun `given not valid payload, when create Person, should return 400`() {
        givenAPerson("") Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should get Person by id`() {
        val id = givenAPerson() Extract {
            path<String>("id.id")
        }

        Given {
            pathParam("id", id)
        } When {
            get("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_OK)
            body("id.id", equalTo(id))
        }
    }

    @Test
    fun `when try getting not existing Person, should return 404`() {
        Given {
            pathParam("id", UUID.randomUUID().toString())
        } When {
            get("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `should update existing Person`() {
        val person = givenAPerson() Extract {
            `as`(PersonDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", person.id!!.id)
            header(HttpHeaders.IF_MATCH, person.id!!.version)
            body(person.copy(name = "John Doe"))
        } When {
            put("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_OK)
            body("name", equalTo("John Doe"))
        }
    }

    @Test
    fun `given not valid payload, when update Person, should return 400`() {
        val person = givenAPerson() Extract {
            `as`(PersonDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", person.id!!.id)
            header(HttpHeaders.IF_MATCH, person.id!!.version)
            body(person.copy(name = ""))
        } When {
            put("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @Test
    fun `when try updating not existing Person, should return 404`() {
        Given {
            contentType(JSON)
            pathParam("id", UUID.randomUUID().toString())
            header(HttpHeaders.IF_MATCH, 0)
            body(PersonDTO("John Doe"))
        } When {
            put("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try updating a Person with wrong version, should return 412`() {
        val person = givenAPerson() Extract {
            `as`(PersonDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", person.id!!.id)
            header(HttpHeaders.IF_MATCH, 100)
            body(person.copy(name = "John Doe"))
        } When {
            put("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }

    @Test
    fun `should delete Person`() {
        val person = givenAPerson() Extract {
            `as`(PersonDTO::class.java)
        }

        Given {
            pathParam("id", person.id!!.id)
            header(HttpHeaders.IF_MATCH, person.id!!.version)
        } When {
            delete("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_OK)
        }
    }

    @Test
    fun `when try deleting not existing Person, should return 404`() {
        Given {
            pathParam("id", UUID.randomUUID().toString())
            header(HttpHeaders.IF_MATCH, 0)
        } When {
            delete("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try deleting a Person with wrong version, should return 412`() {
        val person = givenAPerson() Extract {
            `as`(PersonDTO::class.java)
        }

        Given {
            pathParam("id", person.id!!.id)
            header(HttpHeaders.IF_MATCH, 100)
        } When {
            delete("$PERSONS_URI/{id}")
        } Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }
}
