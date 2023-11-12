package com.ikm.condomanager.domain

/**
 * Enumeration defining the authenticated user roles.
 *
 * @property value the string value of the role as it resides at the auth. server
 */
enum class Role(
    val value: String
) {
    CONDO_MANAGER_USER("condo_manager_user"),

    CONDOMINIUM_MANAGE("condominium_manage"),
    CONDOMINIUM_READ("condominium_read");

    companion object {
        /**
         * @return true if Role with [value] exists, false otherwise
         */
        fun hasValue(value: String): Boolean =
            entries.any { it.value == value }

        /**
         * @return [Role] by given [value]
         * @throws [NoSuchElementException] if [Role] with such value doesn't exist
         */
        fun fromValue(value: String): Role =
            entries.first { it.value == value }
    }
}
