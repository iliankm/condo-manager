package com.ikm.condomanager.adapter.persistence.entity

import com.ikm.condomanager.domain.DomainId
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Version
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant
import java.util.UUID

/**
 * Base JPA entity.
 */
@MappedSuperclass
sealed class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: UUID? = null

    @Version
    @Column(name = "version", nullable = false)
    val version: Long? = null

    val domainId: DomainId?
        get() = if (id != null) DomainId(id.toString(), version) else null

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: Instant? = null

    @Column(name = "created_by", updatable = false)
    var createdBy: String? = null
        private set

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant? = null

    @Column(name = "updated_by")
    var updatedBy: String? = null
        private set

    @PrePersist
    internal fun onPrePersist() {
        createdBy = SecurityContextHolder.getContext()?.authentication?.username
    }

    @PreUpdate
    internal fun onPreUpdate() {
        updatedBy = SecurityContextHolder.getContext()?.authentication?.username
    }
}

private val Authentication.username: String?
    get() = when (this) {
        is JwtAuthenticationToken -> name
        else -> this.principal.toString()
    }
