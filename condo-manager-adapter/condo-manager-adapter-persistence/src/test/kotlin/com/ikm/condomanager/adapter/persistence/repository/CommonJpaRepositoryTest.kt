package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.domain.DomainId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Spring data JPA common test verifying the persistence configuration, optimistic locking, id generation,
 * audit columns etc.
 */
class CommonJpaRepositoryTest : BaseDataJPATest() {
    @Autowired
    private lateinit var personRepository: PersonRepository

    @Test
    fun `should save`() {
        // given
        val personEntity = PersonEntity(
            name = "John Doe",
            email = "john@doe@company.com",
            phoneNumber = "0888111222"
        )
        // when
        val savedPersonEntity = personRepository.saveAndFlush(personEntity)
        // then
        assertNotNull(savedPersonEntity)
        assertTrue(savedPersonEntity.id.toString().isNotEmpty())
        assertEquals(0, savedPersonEntity.version)
        assertNotNull(savedPersonEntity.createdAt)
        assertNotNull(savedPersonEntity.updatedAt)
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should find by DomainId`() {
        // when
        val person =
            personRepository.findByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0))
        // then
        assertTrue(person.isPresent)
        assertEquals(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0), person.get().domainId)
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should find all by EntityId`() {
        // given
        val existing = listOf(
            DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0),
            DomainId("5f92185c-3452-11ee-be56-0242ac120003", 1),
            DomainId("5f92185c-3452-11ee-be56-0242ac120004", 2)
        )
        // when
        val result = personRepository.findAllByDomainId(
            existing +
                listOf(
                    DomainId("5f92185c-3452-11ee-be56-0242ac120000", 0),
                    DomainId("5f92185c-3452-11ee-be56-0242ac120004", 0),
                    DomainId("5f92185c-3452-11ee-be56-0242ac120003", 10),
                )
        )
        // then
        assertEquals(3, result.size)
        assertTrue(result.map { it.domainId }.containsAll(existing))
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should delete by EntityId`() {
        // given
        val entityId = DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0)
        // when
        personRepository.deleteByDomainId(entityId)
        // then
        assertFalse(personRepository.findByDomainId(entityId).isPresent)
    }
}
