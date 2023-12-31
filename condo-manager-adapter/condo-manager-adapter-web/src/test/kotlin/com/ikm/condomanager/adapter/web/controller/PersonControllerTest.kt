package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToPerson
import com.ikm.condomanager.adapter.web.converter.convertToPersonDTO
import com.ikm.condomanager.adapter.web.converter.mergeToPerson
import com.ikm.condomanager.adapter.web.dto.PersonDTO
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.usecase.person.CreatePersonUseCase
import com.ikm.condomanager.usecase.person.DeletePersonUseCase
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.UpdatePersonUseCase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

/**
 * Spring WebMvc test for [PersonController].
 */
@ContextConfiguration(classes = [PersonController::class])
class PersonControllerTest : BaseControllerTest() {
    @MockkBean
    lateinit var createPersonUseCase: CreatePersonUseCase

    @MockkBean
    lateinit var loadPersonUseCase: LoadPersonUseCase

    @MockkBean
    lateinit var updatePersonUseCase: UpdatePersonUseCase

    @MockkBean
    lateinit var deletePersonUseCase: DeletePersonUseCase

    @Test
    fun `should create person resource`() {
        // given
        mockkStatic(PersonDTO::convertToPerson, Person::convertToPersonDTO)
        val personDTO = PersonDTO(name = "John Doe")
        val person = Person(id = null, name = "John Doe")
        val createdPerson = Person(id = PersonId(UUID.randomUUID().toString(), 0), name = "John Doe")
        val createdPersonDTO = PersonDTO(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = createdPerson.name
        )
        every { personDTO.convertToPerson() } returns person
        every { createPersonUseCase.create(person) } returns createdPerson
        every { createdPerson.convertToPersonDTO() } returns createdPersonDTO
        // when
        val result = mvc.perform(
            post("/api/v1/persons")
                .with(jwt())
                .content(personDTO.toJsonString())
                .contentType(APPLICATION_JSON)
        )
        // then
        with(result) {
            andExpect(status().isCreated())
            andExpect(jsonPath("$.id.id").value(createdPersonDTO.id?.id))
        }
        verifyAll {
            personDTO.convertToPerson()
            createPersonUseCase.create(person)
            createdPerson.convertToPersonDTO()
        }
    }

    @Test
    fun `should get person resource by id`() {
        // given
        mockkStatic(Person::convertToPersonDTO)
        val id = UUID.randomUUID().toString()
        val person = Person(
            id = PersonId(id, 0),
            name = "John Doe"
        )
        val personDTO = PersonDTO(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe"
        )
        every { loadPersonUseCase.load(PersonId(id)) } returns person
        every { person.convertToPersonDTO() } returns personDTO
        // when
        val result = mvc.perform(
            get("/api/v1/persons/{id}", id).with(jwt())
        )
        // then
        with(result) {
            andExpect(status().isOk)
            andExpect(jsonPath("$.id.id").value(personDTO.id?.id))
            andExpect(jsonPath("$.id.version").value(0))
        }
        verifyAll {
            loadPersonUseCase.load(PersonId(id))
            person.convertToPersonDTO()
        }
    }

    @Test
    fun `should update person resource`() {
        // given
        mockkStatic(PersonDTO::mergeToPerson, Person::convertToPersonDTO)
        val id = PersonId(UUID.randomUUID().toString(), 1)
        val person = Person(id = id, name = "J..")
        every { loadPersonUseCase.load(id) } returns person
        val personDTO = PersonDTO(name = "John Doe")
        every { personDTO.mergeToPerson(person) } returns Unit
        val updatedPerson = Person(id = PersonId(id.id, 2), name = "John Doe")
        every { updatePersonUseCase.update(person) } returns updatedPerson
        val updatedPersonDTO = PersonDTO(id = PersonId(UUID.randomUUID().toString(), 2), name = "John Doe")
        every { updatedPerson.convertToPersonDTO() } returns updatedPersonDTO
        // when
        val result = mvc.perform(
            put("/api/v1/persons/{id}", id.id)
                .with(jwt())
                .content(personDTO.toJsonString())
                .contentType(APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, 1)
        )
        // then
        with(result) {
            andExpect(status().isOk)
            andExpect(jsonPath("$.id.id").value(updatedPersonDTO.id?.id))
            andExpect(jsonPath("$.id.version").value(2))
        }
        verifyAll {
            loadPersonUseCase.load(id)
            personDTO.mergeToPerson(person)
            updatePersonUseCase.update(person)
            updatedPerson.convertToPersonDTO()
        }
    }

    @Test
    fun `should delete person resource`() {
        // given
        val id = PersonId(UUID.randomUUID().toString(), 1)
        every { deletePersonUseCase.delete(id) } returns Unit
        // when
        val result = mvc.perform(
            delete("/api/v1/persons/{id}", id.id)
                .with(jwt())
                .header(HttpHeaders.IF_MATCH, 1)
        )
        // then
        result.andExpect(status().isOk)
        verifyAll {
            deletePersonUseCase.delete(id)
        }
    }
}
