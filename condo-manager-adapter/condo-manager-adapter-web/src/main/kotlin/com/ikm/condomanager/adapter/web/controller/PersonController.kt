package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToPerson
import com.ikm.condomanager.adapter.web.converter.convertToPersonDTO
import com.ikm.condomanager.adapter.web.converter.mergeToPerson
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.usecase.person.CreatePersonUseCase
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.UpdatePersonUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for person resource.
 */
@RestController
@RequestMapping("api/v1/persons")
class PersonController(
    val createPersonUseCase: CreatePersonUseCase,
    val loadPersonUseCase: LoadPersonUseCase,
    val updatePersonUseCase: UpdatePersonUseCase
) {

    /**
     * POST endpoint for creating a person resource.
     *
     * @param personDTO creation data
     * @return the created person resource
     */
    @PostMapping
    fun createPerson(@RequestBody personDTO: PersonDTO): ResponseEntity<PersonDTO> =
        personDTO.convertToPerson().let {
            createPersonUseCase.create(it)
        }.convertToPersonDTO().let {
            ResponseEntity(it, CREATED)
        }

    /**
     * GET endpoint for getting person resource by its [id].
     *
     * @param id person resource id
     * @return the person resource
     */
    @GetMapping("{id}")
    fun getPerson(@PathVariable("id") id: String): ResponseEntity<PersonDTO> =
        loadPersonUseCase.load(PersonId(id)).convertToPersonDTO().let {
            ResponseEntity(it, OK)
        }

    /**
     * PUT endpoint for updating a person resource.
     *
     * @param id person resource id
     * @param version person resource version
     * @param personDTO update data
     * @return the updated person resource
     */
    @PutMapping("{id}")
    fun updatePerson(
        @PathVariable("id") id: String,
        @RequestHeader(HttpHeaders.IF_MATCH) version: Long,
        @RequestBody personDTO: PersonDTO
    ): ResponseEntity<PersonDTO> =
        loadPersonUseCase.load(PersonId(id, version)).let {
            personDTO.mergeToPerson(it)
        }.let {
            updatePersonUseCase.update(it)
        }.convertToPersonDTO().let {
            ResponseEntity(it, OK)
        }
}
