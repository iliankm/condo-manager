package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.BaseEntity
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.util.Optional
import java.util.UUID

/**
 * Implementation of [BaseRepository].
 */
class BaseRepositoryImpl<T : BaseEntity, ID : Any>(
    entityInformation: JpaEntityInformation<T, *>,
    private val entityManager: EntityManager
) : SimpleJpaRepository<T, ID>(entityInformation, entityManager), BaseRepository<T, ID> {

    private val entityName =
        with(findMergedAnnotation(domainClass, Entity::class.java)) {
            if (this != null && name.isNotEmpty()) name else domainClass.simpleName
        }

    private val findByIdAndVersionQuery = """
        select o from $entityName o where o.id=:id and (:version is NULL or o.version=:version)
    """.trimIndent()

    @Suppress("UNCHECKED_CAST")
    override fun getByDomainId(domainId: DomainId): T {
        checkNotNull(domainId.id)
        return findById(UUID.fromString(domainId.id) as ID)
            .orElseThrow { NotFoundException("$entityName with $domainId not found.") }
            .let {
                if (domainId.version != null && domainId.version != it.version) {
                    throw VersionNotMatchedException(
                        "$entityName current version ${it.version} doesn't match the required ${domainId.version}"
                    )
                }
                it
            }
    }

    override fun findByDomainId(domainId: DomainId): Optional<T> =
        Optional.ofNullable(
            entityManager.createQuery(findByIdAndVersionQuery, domainClass)
                .setParameter("id", UUID.fromString(domainId.id))
                .setParameter("version", domainId.version)
                .resultList.firstOrNull()
        )

    override fun deleteByDomainId(domainId: DomainId) {
        delete(getByDomainId(domainId))
    }
}
