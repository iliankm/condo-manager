package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToPerson
import com.ikm.condomanager.adapter.web.converter.convertToPersonDTO
import com.ikm.condomanager.adapter.web.converter.mergeToPerson
import com.ikm.condomanager.adapter.web.dto.PersonDTO
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.usecase.person.CreatePersonUseCase
import com.ikm.condomanager.usecase.person.DeletePersonUseCase
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.UpdatePersonUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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
    val updatePersonUseCase: UpdatePersonUseCase,
    val deletePersonUseCase: DeletePersonUseCase
) {

    /**
     * POST endpoint for creating a person resource.
     *
     * @param personDTO creation data
     * @return the created person resource
     */
    @Operation(summary = "Create a person resource.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Person created.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = PersonDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Person validation failed.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @PostMapping
    fun createPerson(@RequestBody personDTO: PersonDTO): ResponseEntity<PersonDTO> = personDTO.convertToPerson().let {
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
    @Operation(summary = "Get a person resource by its id.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = PersonDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Person not found.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("{id}")
    fun getPerson(@PathVariable("id") id: String): ResponseEntity<PersonDTO> =
        loadPersonUseCase.load(PersonId(id)).convertToPersonDTO().let {
            ResponseEntity(it, OK)
        }

    /**
     * PUT endpoint for updating a person resource by its id and version.
     *
     * @param id person resource id
     * @param version person resource version
     * @param personDTO update data
     * @return the updated person resource
     */
    @Operation(summary = "Update a person resource by its id and version.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Person updated.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = PersonDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Person validation failed.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Person not found.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "412",
                description = "The current Person's version doesn't match the requested one.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("{id}")
    fun updatePerson(
        @PathVariable("id") id: String,
        @RequestHeader(HttpHeaders.IF_MATCH) version: Long,
        @RequestBody personDTO: PersonDTO
    ): ResponseEntity<PersonDTO> = loadPersonUseCase.load(PersonId(id, version)).let {
        personDTO.mergeToPerson(it)
    }.let {
        updatePersonUseCase.update(it)
    }.convertToPersonDTO().let {
        ResponseEntity(it, OK)
    }

    /**
     * DELETE endpoint for deleting a person resource.
     *
     * @param id person resource id
     * @param version person resource version
     */
    @Operation(summary = "Delete a person resource by its id and version.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Person deleted.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = PersonDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Person not found.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "412",
                description = "The current Person's version doesn't match the requested one.",
                content = [
                    Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @DeleteMapping("{id}")
    fun deletePerson(
        @PathVariable("id") id: String,
        @RequestHeader(HttpHeaders.IF_MATCH) version: Long
    ): ResponseEntity<Unit> {
        deletePersonUseCase.delete(PersonId(id, version))
        return ResponseEntity(OK)
    }
}
