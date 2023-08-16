package com.ikm.condomanager.infra.configuration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * Spring configuration for [ObjectMapper].
 */
@Configuration
class ObjectMapperConfiguration {

    /**
     * Bean producer for [ObjectMapper] configured for:
     * 1. Do not serialize properties with null value,
     * 2. Register [JavaTimeModule] for serializing [java.time] objects
     * 3. Configure writing dates/date-time as string in the form YYYY-MM-DDTHH:MM:SS
     */
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper =
        ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}
