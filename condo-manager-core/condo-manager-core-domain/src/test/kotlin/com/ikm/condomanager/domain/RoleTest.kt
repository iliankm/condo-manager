package com.ikm.condomanager.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Unit test for [Role] enum.
 */
class RoleTest {
    @ParameterizedTest
    @MethodSource("roles")
    fun `should create Role entries`(role: Role, expectedValue: String) {
        assertEquals(expectedValue, role.value)
    }

    companion object {
        @JvmStatic
        fun roles() =
            Role.entries.map {
                when (it) {
                    Role.CONDO_MANAGER_USER -> Arguments.of(it, "condo_manager_user")
                    Role.CREATE_PERSON -> Arguments.of(it, "create_person")
                    Role.READ_PERSON -> Arguments.of(it, "read_person")
                    Role.UPDATE_PERSON -> Arguments.of(it, "update_person")
                    Role.DELETE_PERSON -> Arguments.of(it, "delete_person")
                    Role.CREATE_CONDOMINIUM -> Arguments.of(it, "create_condominium")
                    Role.READ_CONDOMINIUM -> Arguments.of(it, "read_condominium")
                    Role.UPDATE_CONDOMINIUM -> Arguments.of(it, "update_condominium")
                    Role.DELETE_CONDOMINIUM -> Arguments.of(it, "delete_condominium")
                }
            }
    }
}
