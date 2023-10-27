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

    CREATE_PERSON("create_person"),
    READ_PERSON("read_person"),
    UPDATE_PERSON("update_person"),
    DELETE_PERSON("delete_person"),

    CREATE_CONDOMINIUM("create_condominium"),
    READ_CONDOMINIUM("read_condominium"),
    UPDATE_CONDOMINIUM("update_condominium"),
    DELETE_CONDOMINIUM("delete_condominium")
}
