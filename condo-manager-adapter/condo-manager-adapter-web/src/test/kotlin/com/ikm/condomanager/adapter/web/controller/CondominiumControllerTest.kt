package com.ikm.condomanager.adapter.web.controller

import com.ikm.condomanager.adapter.web.converter.convertToCondominium
import com.ikm.condomanager.adapter.web.converter.convertToCondominiumDTO
import com.ikm.condomanager.adapter.web.converter.mergeToCondominium
import com.ikm.condomanager.adapter.web.dto.CondominiumAddressDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumCreateDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumDTO
import com.ikm.condomanager.adapter.web.dto.CondominiumUpdateDTO
import com.ikm.condomanager.domain.Condominium
import com.ikm.condomanager.domain.CondominiumId
import com.ikm.condomanager.usecase.condominium.CreateCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.DeleteCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.LoadCondominiumUseCase
import com.ikm.condomanager.usecase.condominium.UpdateCondominiumUseCase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
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
        mockkStatic(CondominiumCreateDTO::convertToCondominium, Condominium::convertToCondominiumDTO)
        val condominiumCreateDTO = CondominiumCreateDTO(
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        val condominium = mockk<Condominium>()
        every { condominiumCreateDTO.convertToCondominium() } returns condominium
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
            MockMvcRequestBuilders.post("/api/v1/condominiums")
                .content(condominiumCreateDTO.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)
        )
        // then
        with(result) {
            andExpect(status().isCreated())
            andExpect(jsonPath("$.id.id").value(createdCondominiumDTO.id.id))
        }
        verifyAll {
            condominiumCreateDTO.convertToCondominium()
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
            MockMvcRequestBuilders.get("/api/v1/condominiums/{id}", id)
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
        mockkStatic(CondominiumUpdateDTO::mergeToCondominium, Condominium::convertToCondominiumDTO)
        val id = CondominiumId(UUID.randomUUID().toString(), 1)
        val condominiumUpdateDTO = CondominiumUpdateDTO(
            address = CondominiumAddressDTO(
                city = "City Name",
                street = "Street Name",
                houseNumber = 1,
                location = null
            )
        )
        val condominium = mockk<Condominium>()
        every { loadCondominiumUseCase.load(id) } returns condominium
        every { condominiumUpdateDTO.mergeToCondominium(condominium) } returns Unit
        val updatedCondominium = mockk<Condominium>()
        every { updateCondominiumUseCase.update(condominium) } returns updatedCondominium
        val condominiumDTO = CondominiumDTO(
            id = CondominiumId(id.id, 2),
            address = condominiumUpdateDTO.address
        )
        every { updatedCondominium.convertToCondominiumDTO() } returns condominiumDTO
        // when
        val result = mvc.perform(
            MockMvcRequestBuilders.put("/api/v1/condominiums/{id}", id.id)
                .content(condominiumUpdateDTO.toJsonString())
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.IF_MATCH, id.version)
        )
        // then
        with(result) {
            andExpect(status().isOk)
            andExpect(jsonPath("$.id.id").value(condominiumDTO.id.id))
            andExpect(jsonPath("$.id.version").value(condominiumDTO.id.version))
        }
        verifyAll {
            loadCondominiumUseCase.load(id)
            condominiumUpdateDTO.mergeToCondominium(condominium)
            updateCondominiumUseCase.update(condominium)
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
            MockMvcRequestBuilders.delete("/api/v1/condominiums/{id}", id.id)
                .header(HttpHeaders.IF_MATCH, id.version)
        )
        // then
        result.andExpect(status().isOk)
        verifyAll {
            deleteCondominiumUseCase.delete(id)
        }
    }
}
