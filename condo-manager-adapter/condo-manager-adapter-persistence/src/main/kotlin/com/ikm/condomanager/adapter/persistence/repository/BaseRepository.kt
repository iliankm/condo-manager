package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.domain.DomainId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.Optional

/**
 * Base repository for all Spring Data JPA repositories.
 * The purpose of this base interface is to have common methods like:
 * [findByDomainId], [findAllByDomainId], [deleteByDomainId] etc. in all repositories.
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
     * Returns all instances of the type [T] with the given [ids].
     * If some or all ids are not found, no entities are returned for these [ids].
     * The order of elements in the result is not guaranteed.
     *
     * @param ids
     * @return guaranteed to be not null. The size can be equal or less than the number of given ids.
     */
    fun findAllByDomainId(ids: Iterable<DomainId>): List<T>

    /**
     * Deletes the entity with the given [domainId].
     * If the entity is not found in the persistence store it is silently ignored.
     *
     * @param domainId
     */
    fun deleteByDomainId(domainId: DomainId)
}
