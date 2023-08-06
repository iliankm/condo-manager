package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.EntityId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

/**
 * Base repository for all Spring Data JPA repositories.
 * The purpose of this base interface is to have common methods like:
 * [findByEntityId], [findAllByEntityId], [deleteByEntityId], [deleteAllByEntityId] etc. in all repositories.
 */
@NoRepositoryBean
interface BaseRepository<T, ID> : JpaRepository<T, ID> {
    /**
     * Retrieves an entity by its [entityId].
     *
     * @param entityId
     * @return the entity or [Optional.empty] if not found.
     */
    fun findByEntityId(entityId: EntityId): Optional<T & Any>

    /**
     * Returns all instances of the type [T] with the given [ids].
     * If some or all ids are not found, no entities are returned for these [ids].
     * The order of elements in the result is not guaranteed.
     *
     * @param ids
     * @return guaranteed to be not null. The size can be equal or less than the number of given ids.
     */
    fun findAllByEntityId(ids: Iterable<EntityId>): List<T>
}
