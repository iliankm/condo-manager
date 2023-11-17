package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToCondominium
import com.ikm.condomanager.adapter.web.converter.convertToCondominiumDTO
import com.ikm.condomanager.adapter.web.converter.convertToUpdateCondominiumData
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
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
 * REST resource for condominium resource.
 */
@RestController
@RequestMapping("api/v1/condominiums")
class CondominiumController(
    val createCondominiumUseCase: CreateCondominiumUseCase,
    val loadCondominiumUseCase: LoadCondominiumUseCase,
    val updateCondominiumUseCase: UpdateCondominiumUseCase,
    val deleteCondominiumUseCase: DeleteCondominiumUseCase
) {
    /**
     * POST endpoint for creating a condominium resource.
     *
     * @param condominiumDTO condominium creation data
     * @return the created condominium resource
     */
    @Operation(summary = "Create a condominium resource.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Condominium created.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CondominiumDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Condominium validation failed.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized."
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden."
            )
        ]
    )
    @PostMapping
    fun createCondominium(@RequestBody condominiumDTO: CondominiumDTO): ResponseEntity<CondominiumDTO> =
        condominiumDTO.convertToCondominium().let {
            createCondominiumUseCase.create(it)
        }.convertToCondominiumDTO().let {
            ResponseEntity(it, CREATED)
        }

    /**
     * GET endpoint for getting condominium resource by its [id].
     *
     * @param id condominium resource id
     * @return the condominium resource
     */
    @Operation(summary = "Get a condominium resource by its id.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CondominiumDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized."
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden."
            ),
            ApiResponse(
                responseCode = "404",
                description = "Condominium not found.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("{id}")
    fun getCondominium(@PathVariable("id") id: String): ResponseEntity<CondominiumDTO> =
        loadCondominiumUseCase.load(PersonId(id)).convertToCondominiumDTO().let {
            ResponseEntity(it, OK)
        }

    /**
     * PUT endpoint for updating a condominium resource by its id and version.
     *
     * @param id condominium resource id
     * @param version condominium resource version
     * @param condominiumDTO update data
     * @return the updated condominium resource
     */
    @Operation(summary = "Update a condominium resource by its id and version.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Condominium updated.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CondominiumDTO::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Condominium validation failed.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized."
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden."
            ),
            ApiResponse(
                responseCode = "404",
                description = "Condominium not found.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "412",
                description = "The current Condominium's version doesn't match the requested one.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("{id}")
    fun updateCondominium(
        @PathVariable("id") id: String,
        @RequestHeader(HttpHeaders.IF_MATCH) version: Long,
        @RequestBody condominiumDTO: CondominiumDTO
    ): ResponseEntity<CondominiumDTO> =
        condominiumDTO.convertToUpdateCondominiumData().let {
            updateCondominiumUseCase.update(CondominiumId(id, version), it)
        }.convertToCondominiumDTO().let {
            ResponseEntity(it, OK)
        }

    /**
     * DELETE endpoint for deleting a condominium resource.
     *
     * @param id condominium resource id
     * @param version condominium resource version
     */
    @Operation(summary = "Delete a condominium resource by its id and version.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Condominium deleted.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = Unit::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized."
            ),
            ApiResponse(
                responseCode = "403",
                description = "Forbidden."
            ),
            ApiResponse(
                responseCode = "404",
                description = "Condominium not found.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "412",
                description = "The current Condominium's version doesn't match the requested one.",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ProblemDetail::class)
                    )
                ]
            )
        ]
    )
    @DeleteMapping("{id}")
    fun deleteCondominium(
        @PathVariable("id") id: String,
        @RequestHeader(HttpHeaders.IF_MATCH) version: Long
    ): ResponseEntity<Unit> {
        deleteCondominiumUseCase.delete(CondominiumId(id, version))
        return ResponseEntity(OK)
    }
}
