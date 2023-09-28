package com.ikm.condomanager

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

/**
 * Integration test which is considered to be executed last (after all other test classes).
 * Its purpose is to stop the containers started for the integration tests.
 */
@Order(Int.MAX_VALUE)
class ShutdownContainersIT : BaseIntegrationTest() {
    companion object {
        @JvmStatic
        @AfterAll
        fun afterAll() {
            environment.stop()
        }
    }

    @Test
    fun `dummy test`() {
        assertNotNull(environment)
    }
}
