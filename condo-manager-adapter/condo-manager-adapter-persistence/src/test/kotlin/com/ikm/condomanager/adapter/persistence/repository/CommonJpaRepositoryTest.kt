package com.ikm.condomanager.adapter.persistence.repository

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.domain.DomainId
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun `should get by DomainId without version`() {
        // when
        val person = personRepository.getByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002"))
        // then
        assertEquals(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0), person.domainId)
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should get by DomainId with version`() {
        // when
        val person = personRepository.getByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0))
        // then
        assertEquals(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0), person.domainId)
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should throw NotFoundException`() {
        // when & then
        assertThrows<NotFoundException> {
            personRepository.getByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac121111", 0))
        }
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should throw VersionNotMatchedException`() {
        // when & then
        assertThrows<VersionNotMatchedException> {
            personRepository.getByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 10))
        }
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `should find by DomainId`() {
        // when
        val person =
            personRepository.findByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002"))
        // then
        assertTrue(person.isPresent)
        assertEquals(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 0), person.get().domainId)
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

    @Test
    @Sql("/sql/create-person.sql")
    fun `when delete by not existing id, should throw NotFoundException`() {
        // when & then
        assertThrows<NotFoundException> {
            personRepository.deleteByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac121111", 0))
        }
    }

    @Test
    @Sql("/sql/create-person.sql")
    fun `when delete but the version is not matched, should throw VersionNotMatchedException`() {
        // when & then
        assertThrows<VersionNotMatchedException> {
            personRepository.deleteByDomainId(DomainId("5f92185c-3452-11ee-be56-0242ac120002", 10))
        }
    }
}
