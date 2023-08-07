package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.EntityId
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.util.*

/**
 * Implementation of [BaseRepository].
 */
class BaseRepositoryImpl<T, ID>(
    entityInformation: JpaEntityInformation<T, *>,
    private val entityManager: EntityManager
) : SimpleJpaRepository<T, ID>(entityInformation, entityManager), BaseRepository<T, ID> {

    private val entityName =
        with(findMergedAnnotation(domainClass, Entity::class.java)) {
            if (this != null && name.isNotEmpty()) name else domainClass.simpleName
        }

    private val findByIdAndVersionQuery = """
        select o from $entityName o where o.id=:id and o.version=:version
    """.trimIndent()

    private val findByIdsAndVersionQuery = """
        select o from $entityName o where CONCAT(o.id, '_ver_', o.version) IN :param
    """.trimIndent()

    override fun findByEntityId(entityId: EntityId): Optional<T & Any> =
        Optional.ofNullable(
            entityManager.createQuery(findByIdAndVersionQuery, domainClass)
                .setParameter("id", entityId.id)
                .setParameter("version", entityId.version)
                .resultList.firstOrNull()
        )

    override fun findAllByEntityId(ids: Iterable<EntityId>): List<T> =
        entityManager.createQuery(findByIdsAndVersionQuery, domainClass)
            .setParameter("param", ids.map { it.id.toString() + "_ver_" + it.version })
            .resultList

    override fun deleteByEntityId(entityId: EntityId) {
        findByEntityId(entityId).ifPresent { delete(it) }
    }
}
