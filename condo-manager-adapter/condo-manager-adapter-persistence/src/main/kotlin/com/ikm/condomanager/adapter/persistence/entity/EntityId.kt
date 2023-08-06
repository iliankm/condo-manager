package com.ikm.condomanager.adapter.persistence.entity

/**
 * Immutable data class representing JPA entity id.
 */
data class EntityId(
    val id: String,
    val version: Long
)
