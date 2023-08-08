package com.ikm.condomanager.adapter.persistence.converter

import com.ikm.condomanager.adapter.persistence.entity.PersonEntity
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [Person] related conversions.
 */
class PersonConverterTest {
    @Test
    fun `should convert Person to PersonEntity`() {
        // given
        val person = Person(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe",
            email = "john.doe@company.com",
            phoneNumber = "0888111222"
        )
        // when
        val personEntity = person.convertToPersonEntity()
        // then
        assertEquals(person.name, personEntity.name)
        assertEquals(person.email, personEntity.email)
        assertEquals(person.phoneNumber, personEntity.phoneNumber)
    }

    @Test
    fun `should merge Person to PersonEntity`() {
        // given
        val person = Person(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe",
            email = "john.doe@company.com",
            phoneNumber = "0888111222"
        )
        val personEntity = PersonEntity(
            name = ""
        )
        // when
        val merged = person.mergeToPersonEntity(personEntity)
        // then
        assertSame(personEntity, merged)
        assertEquals("John Doe", personEntity.name)
        assertEquals("john.doe@company.com", personEntity.email)
        assertEquals("0888111222", personEntity.phoneNumber)
    }

    @Test
    fun `should convert PersonEntity to Person`() {
        // given
        val personEntity = PersonEntity(
            name = "John Doe",
            email = "john.doe@company.com",
            phoneNumber = "0888111222"
        )
        // when
        val person = personEntity.convertToPerson()
        // then
        assertNull(person.id)
        assertEquals("John Doe", person.name)
        assertEquals("john.doe@company.com", person.email)
        assertEquals("0888111222", person.phoneNumber)
    }
}
