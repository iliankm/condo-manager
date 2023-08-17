package com.ikm.condomanager.exception

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Unit test for [VersionNotMatchedException].
 */
class VersionNotMatchedExceptionTest {
    @Test
    fun `should throw VersionNotMatchedException`() {
        val exception = assertThrows(VersionNotMatchedException::class.java) {
            throw VersionNotMatchedException("Test message")
        }
        assertEquals("Test message", exception.message)
    }
}
