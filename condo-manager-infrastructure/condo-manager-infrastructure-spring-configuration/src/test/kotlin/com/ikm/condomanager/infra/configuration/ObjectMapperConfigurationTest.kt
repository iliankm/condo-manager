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
import kotlin.test.assertNull

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

    @Test
    fun `should deserialize data classes`() {
        // given
        val json = """{"p1":"aaa","p3":"1977-05-24","p4":"1977-05-24T13:30:00"}"""
        // when
        val result = objectMapper.readValue(json, TestData::class.java)
        // then
        with(result) {
            assertEquals("aaa", p1)
            assertNull(p2)
            assertEquals(LocalDate.of(1977, 5, 24), p3)
            assertEquals(LocalDateTime.of(1977, 5, 24, 13, 30, 0), p4)
        }
    }
}

private data class TestData(
    val p1: String? = null,
    val p2: String? = null,
    val p3: LocalDate? = null,
    val p4: LocalDateTime? = null
)
