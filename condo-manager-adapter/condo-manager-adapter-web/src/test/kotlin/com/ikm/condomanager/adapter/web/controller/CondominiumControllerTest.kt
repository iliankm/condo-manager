package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToCondominium
import com.ikm.condomanager.adapter.web.converter.convertToCondominiumDTO
import com.ikm.condomanager.adapter.web.converter.convertToUpdateCondominiumData
import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumData
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
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
 * Spring WebMvc test for [CondominiumController].
 */
@ContextConfiguration(classes = [CondominiumController::class])
class CondominiumControllerTest : BaseControllerTest() {
    @MockkBean
    lateinit var createCondominiumUseCase: CreateCondominiumUseCase

    @MockkBean
    lateinit var loadCondominiumUseCase: LoadCondominiumUseCase

    @MockkBean
    lateinit var updateCondominiumUseCase: UpdateCondominiumUseCase

    @MockkBean
    lateinit var deleteCondominiumUseCase: DeleteCondominiumUseCase

    @Test
    fun `should create condominium resource`() {
        // given
        mockkStatic(CondominiumDTO::convertToCondominium, Condominium::convertToCondominiumDTO)
        val condominiumDTO = CondominiumDTO(
            id = null,
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        val condominium = mockk<Condominium>()
        every { condominiumDTO.convertToCondominium() } returns condominium
        every { createCondominiumUseCase.create(condominium) } returns condominium
        val createdCondominiumDTO = CondominiumDTO(
            id = CondominiumId(UUID.randomUUID().toString(), 1),
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        every { condominium.convertToCondominiumDTO() } returns createdCondominiumDTO
        // when
        val result = mvc.perform(
            post("/api/v1/condominiums")
                .with(jwt())
                .content(condominiumDTO.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)
        )
        // then
        with(result) {
            andExpect(status().isCreated())
            andExpect(jsonPath("$.id.id").value(createdCondominiumDTO.id!!.id))
        }
        verifyAll {
            condominiumDTO.convertToCondominium()
            createCondominiumUseCase.create(condominium)
            condominium.convertToCondominiumDTO()
        }
    }

    @Test
    fun `should get condominium resource by id`() {
        // given
        mockkStatic(Condominium::convertToCondominiumDTO)
        val id = UUID.randomUUID().toString()
        val condominium = mockk<Condominium>()
        every { loadCondominiumUseCase.load(CondominiumId(id)) } returns condominium
        val condominiumDTO = CondominiumDTO(
            id = CondominiumId(id, 1),
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        every { condominium.convertToCondominiumDTO() } returns condominiumDTO
        // when
        val result = mvc.perform(
            get("/api/v1/condominiums/{id}", id).with(jwt())
        )
        // then
        with(result) {
            andExpect(status().isOk)
            andExpect(jsonPath("$.id.id").value(id))
            andExpect(jsonPath("$.id.version").value(1))
        }
        verifyAll {
            loadCondominiumUseCase.load(CondominiumId(id))
            condominium.convertToCondominiumDTO()
        }
    }

    @Test
    fun `should update condominium resource`() {
        // given
        mockkStatic(CondominiumDTO::convertToUpdateCondominiumData, Condominium::convertToCondominiumDTO)
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        val condominiumDTO = CondominiumDTO(
            id = id,
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        val updatedCondominium = mockk<Condominium>()
        val updateCondominiumData = mockk<UpdateCondominiumData>()
        every { condominiumDTO.convertToUpdateCondominiumData() } returns updateCondominiumData
        every { updateCondominiumUseCase.update(id, updateCondominiumData) } returns updatedCondominium
        val updatedCondominiumDTO = CondominiumDTO(
            id = CondominiumId(id.id, 2),
            address = condominiumDTO.address
        )
        every { updatedCondominium.convertToCondominiumDTO() } returns updatedCondominiumDTO
        // when
        val result = mvc.perform(
            put("/api/v1/condominiums/{id}", id.id)
                .with(jwt())
                .content(condominiumDTO.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, id.version)
        )
        // then
        with(result) {
            andExpect(status().isOk)
            andExpect(jsonPath("$.id.id").value(condominiumDTO.id?.id))
            andExpect(jsonPath("$.id.version").value(2))
        }
        verifyAll {
            condominiumDTO.convertToUpdateCondominiumData()
            updateCondominiumUseCase.update(id, updateCondominiumData)
            updatedCondominium.convertToCondominiumDTO()
        }
    }

    @Test
    fun `should delete condominium resource`() {
        // given
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        every { deleteCondominiumUseCase.delete(id) } returns Unit
        // when
        val result = mvc.perform(
            delete("/api/v1/condominiums/{id}", id.id)
                .with(jwt())
                .header(HttpHeaders.IF_MATCH, id.version)
        )
        // then
        result.andExpect(status().isOk)
        verifyAll {
            deleteCondominiumUseCase.delete(id)
        }
    }
}
