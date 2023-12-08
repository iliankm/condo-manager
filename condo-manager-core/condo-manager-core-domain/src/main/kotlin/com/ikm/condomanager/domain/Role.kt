package com.ikm.condomanager.domain

/**
 * Enumeration defining the authenticated user roles.
 *
 * @property value the string value of the role as it resides at the auth. server
 */
enum class Role(
    val value: String
) {
    MONITORING(Names.MONITORING),
    CONDO_MANAGER_USER(Names.CONDO_MANAGER_USER),

    CONDOMINIUM_MANAGE(Names.CONDOMINIUM_MANAGE),
    CONDOMINIUM_READ(Names.CONDOMINIUM_READ);

    /**
     * Auth. server role names.
     */
    object Names {
        const val MONITORING = "monitoring"
        const val CONDO_MANAGER_USER = "condo_manager_user"
        const val CONDOMINIUM_MANAGE = "condominium_manage"
        const val CONDOMINIUM_READ = "condominium_read"
    }

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
