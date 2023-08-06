package com.ikm.condomanager.adapter.persistence.entity

import java.util.UUID

/**
 * Immutable data class representing JPA entity id.
 */
data class EntityId(
    val id: UUID,
    val version: Long
)
