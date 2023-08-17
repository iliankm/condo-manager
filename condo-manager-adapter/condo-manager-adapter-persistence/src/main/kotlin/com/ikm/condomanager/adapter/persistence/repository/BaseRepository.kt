package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.BaseEntity
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.Optional

/**
 * Base repository for all Spring Data JPA repositories.
 * The purpose of this base interface is to have common methods like:
 * [findByDomainId], [deleteByDomainId] etc. in all repositories.
 */
@NoRepositoryBean
interface BaseRepository<T : BaseEntity, ID> : JpaRepository<T, ID> {
    /**
     * Retrieves an entity by its [domainId].
     *
     * @param domainId
     * @return the entity if found and its version is matched, otherwise throws exception
     * @throws NotFoundException if the entity by [DomainId.id] was not found
     * @throws VersionNotMatchedException if [DomainId.version] is passed, but it doesn't match version of the entity
     */
    fun getByDomainId(domainId: DomainId): T

    /**
     * Retrieves an entity by its [domainId].
     *
     * @param domainId
     * @return the entity or [Optional.empty] if not found.
     */
    fun findByDomainId(domainId: DomainId): Optional<T>

    /**
     * Deletes the entity with the given [domainId] if it is found and its version matches.
     *
     * @param domainId
     * @throws NotFoundException if the entity by [DomainId.id] was not found
     * @throws VersionNotMatchedException if [DomainId.version] is passed, but it doesn't match version of the entity
     */
    fun deleteByDomainId(domainId: DomainId)
}
