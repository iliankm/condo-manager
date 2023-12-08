package com.ikm.condomanager.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit test for [Role] enum.
 */
class RoleTest {
    @ParameterizedTest
    @MethodSource("roles")
    fun `should create Role entries`(role: Role, expectedValue: String) {
        assertEquals(expectedValue, role.value)
    }

    @ParameterizedTest
    @MethodSource("roles")
    fun `should create from value`(role: Role, value: String) {
        assertTrue(Role.hasValue(value))

        assertEquals(role, Role.fromValue(value))
    }

    @Test
    fun `should throw NoSuchElementException`() {
        assertFalse(Role.hasValue("aaa"))

        assertThrows<NoSuchElementException> {
            Role.fromValue("aaa")
        }
    }

    companion object {
        @JvmStatic
        fun roles() =
            Role.entries.map {
                when (it) {
                    Role.MONITORING -> Arguments.of(it, "monitoring")
                    Role.CONDO_MANAGER_USER -> Arguments.of(it, "condo_manager_user")
                    Role.CONDOMINIUM_MANAGE -> Arguments.of(it, "condominium_manage")
                    Role.CONDOMINIUM_READ -> Arguments.of(it, "condominium_read")
                }
            }
    }
}
