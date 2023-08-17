package com.ikm.condomanager.adapter.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ikm.condomanager.infra.configuration.ObjectMapperConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

/**
 * Base WebMvc test.
 */
@WebMvcTest
@Import(ObjectMapperConfiguration::class)
sealed class BaseControllerTest {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    /**
     * Helper function to convert [Any] object to json string.
     */
    fun Any.toJsonString(): String =
        objectMapper.writeValueAsString(this)
}
