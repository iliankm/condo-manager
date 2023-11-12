package com.ikm.condomanager.domain

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * Unit test for [AuthenticatedUser].
 */
class AuthenticatedUserTest {
    @Test
    fun `should create AuthenticatedUser`() {
        // given & when
        val user = AuthenticatedUser(
            username = "User",
            email = "user@domain.com",
            firstName = "John",
            lastName = "Doe",
            roles = setOf(Role.CONDO_MANAGER_USER)
        )
        // then
        with(user) {
            assertEquals("User", username)
            assertEquals("user@domain.com", email)
            assertEquals("John", firstName)
            assertEquals("Doe", lastName)
            assertEquals(setOf(Role.CONDO_MANAGER_USER), roles)
        }
    }
}
