package com.ikm.condomanager.infra.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Spring test for [ObjectMapperConfiguration].
 */
@ExtendWith(SpringExtension::class)
@Import(ObjectMapperConfiguration::class)
class ObjectMapperConfigurationTest {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should not serialize nulls`() {
        // given
        val obj = TestData(p2 = "ddd")
        // when
        val result = objectMapper.writeValueAsString(obj)
        // then
        assertEquals("""{"p2":"ddd"}""", result)
    }

    @Test
    fun `should serialize LocalDateTime as String`() {
        // given
        val obj = TestData(
            p3 = LocalDate.of(1977, 5, 24),
            p4 = LocalDateTime.of(1977, 5, 24, 13, 30, 0)
        )
        // when
        val result = objectMapper.writeValueAsString(obj)
        // then
        assertEquals("""{"p3":"1977-05-24","p4":"1977-05-24T13:30:00"}""", result)
    }
}

private data class TestData(
    val p1: String? = null,
    val p2: String? = null,
    val p3: LocalDate? = null,
    val p4: LocalDateTime? = null
)
