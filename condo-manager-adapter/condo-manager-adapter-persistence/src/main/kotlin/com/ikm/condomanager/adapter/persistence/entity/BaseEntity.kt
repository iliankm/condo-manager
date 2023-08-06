package com.ikm.condomanager.adapter.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.Version
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*

/**
 * Base JPA entity.
 */
@MappedSuperclass
sealed class BaseEntity {
    @Id
    @Column(name = "id", updatable = false, unique = true, nullable = false, length = 36)
    var id: String? = null
        private set

    @Version
    @Column(name = "version", nullable = false)
    val version: Long? = null

    val entityId: EntityId?
        get() = if (id != null && version != null) EntityId(id!!, version) else null

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: Instant? = null

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null

    @PrePersist
    fun generateId() {
        if (id == null) {
            id = UUID.randomUUID().toString()
        }
    }
}
