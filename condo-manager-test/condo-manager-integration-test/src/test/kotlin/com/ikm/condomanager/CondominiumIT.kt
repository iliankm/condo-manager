package com.ikm.condomanager

import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumCreateDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumUpdateDTO
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
 * Integration test for Condominium resource.
 */
class CondominiumIT : BaseIntegrationTest() {
    companion object {
        private const val CONDOMINIUMS_URI = "api/v1/condominiums"
        private fun givenACondominium() =
            Given {
                contentType(JSON)
                body(
                    CondominiumCreateDTO(
                        address = CondominiumAddressDTO(
                            city = randomAlphabetic(20),
                            street = randomAlphabetic(20),
                            houseNumber = 1,
                            location = null
                        )
                    )
                )
            } When {
                post(CONDOMINIUMS_URI)
            }
    }

    @Test
    fun `should create Condominium`() {
        givenACondominium() Then {
            statusCode(SC_CREATED)
        }
    }

    @Test
    fun `given not valid payload, when create Condominium, should return 400`() {
        Given {
            contentType(JSON)
            body(
                CondominiumCreateDTO(
                    address = CondominiumAddressDTO(
                        city = "",
                        street = "",
                        houseNumber = 1,
                        location = null
                    )
                )
            )
        } When {
            post(CONDOMINIUMS_URI)
        } Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should get Condominium by id`() {
        val id = givenACondominium() Extract {
            path<String>("id.id")
        }

        Given {
            pathParam("id", id)
        } When {
            get("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_OK)
            body("id.id", equalTo(id))
        }
    }

    @Test
    fun `when try getting not existing Condominium, should return 404`() {
        Given {
            pathParam("id", UUID.randomUUID().toString())
        } When {
            get("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `should update existing Condominium`() {
        val condominiumDTO = givenACondominium() Extract {
            `as`(CondominiumDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", condominiumDTO.id.id)
            header(HttpHeaders.IF_MATCH, condominiumDTO.id.version)
            body(
                CondominiumUpdateDTO(
                    address = condominiumDTO.address.copy(city = "Some City")
                )
            )
        } When {
            put("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_OK)
            body("address.city", equalTo("Some City"))
        }
    }

    @Test
    fun `given not valid payload, when update Condominium, should return 400`() {
        val condominiumDTO = givenACondominium() Extract {
            `as`(CondominiumDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", condominiumDTO.id.id)
            header(HttpHeaders.IF_MATCH, condominiumDTO.id.version)
            body(
                CondominiumUpdateDTO(
                    address = condominiumDTO.address.copy(city = "")
                )
            )
        } When {
            put("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_BAD_REQUEST)
        }
    }

    @Test
    fun `when try updating not existing Condominium, should return 404`() {
        Given {
            contentType(JSON)
            pathParam("id", UUID.randomUUID().toString())
            header(HttpHeaders.IF_MATCH, 0)
            body(
                CondominiumUpdateDTO(
                    address = CondominiumAddressDTO(
                        city = randomAlphabetic(20),
                        street = randomAlphabetic(20),
                        houseNumber = 1,
                        location = null
                    )
                )
            )
        } When {
            put("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try updating a Condominium with wrong version, should return 412`() {
        val condominiumDTO = givenACondominium() Extract {
            `as`(CondominiumDTO::class.java)
        }

        Given {
            contentType(JSON)
            pathParam("id", condominiumDTO.id.id)
            header(HttpHeaders.IF_MATCH, 100)
            body(
                CondominiumUpdateDTO(
                    address = condominiumDTO.address.copy()
                )
            )
        } When {
            put("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }

    @Test
    fun `should delete Condominium`() {
        val condominiumDTO = givenACondominium() Extract {
            `as`(CondominiumDTO::class.java)
        }

        Given {
            pathParam("id", condominiumDTO.id.id)
            header(HttpHeaders.IF_MATCH, condominiumDTO.id.version)
        } When {
            delete("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_OK)
        }
    }

    @Test
    fun `when try deleting not existing Condominium, should return 404`() {
        Given {
            pathParam("id", UUID.randomUUID().toString())
            header(HttpHeaders.IF_MATCH, 0)
        } When {
            delete("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_NOT_FOUND)
        }
    }

    @Test
    fun `when try deleting a Condominium with wrong version, should return 412`() {
        val condominiumDTO = givenACondominium() Extract {
            `as`(CondominiumDTO::class.java)
        }

        Given {
            pathParam("id", condominiumDTO.id.id)
            header(HttpHeaders.IF_MATCH, 100)
        } When {
            delete("$CONDOMINIUMS_URI/{id}")
        } Then {
            statusCode(SC_PRECONDITION_FAILED)
        }
    }
}
