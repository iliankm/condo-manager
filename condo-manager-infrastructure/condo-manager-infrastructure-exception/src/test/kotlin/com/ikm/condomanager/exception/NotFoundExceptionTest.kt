package com.ikm.condomanager.exception

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Unit test for [NotFoundException].
 */
class NotFoundExceptionTest {
    @Test
    fun `should throw NotFoundException`() {
        val exception = assertThrows(NotFoundException::class.java) {
            throw NotFoundException("Test message")
        }
        assertEquals("Test message", exception.message)
    }
}
