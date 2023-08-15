package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.domain.DomainId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.Optional

/**
 * Base repository for all Spring Data JPA repositories.
 * The purpose of this base interface is to have common methods like:
 * [findByDomainId], [deleteByDomainId] etc. in all repositories.
 */
@NoRepositoryBean
interface BaseRepository<T, ID> : JpaRepository<T, ID> {
    /**
     * Retrieves an entity by its [domainId].
     *
     * @param domainId
     * @return the entity or [Optional.empty] if not found.
     */
    fun findByDomainId(domainId: DomainId): Optional<T & Any>

    /**
     * Deletes the entity with the given [domainId].
     * If the entity is not found in the persistence store it is silently ignored.
     *
     * @param domainId
     */
    fun deleteByDomainId(domainId: DomainId)
}
