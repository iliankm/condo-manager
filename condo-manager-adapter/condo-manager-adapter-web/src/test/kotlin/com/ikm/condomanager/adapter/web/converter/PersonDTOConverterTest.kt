package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.dto.PersonDTO
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit test for [PersonDTO] related conversions.
 */
class PersonDTOConverterTest {
    @Test
    fun `should convert PersonDTO to Person`() {
        // given
        val personDTO = PersonDTO(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe",
            email = "john.doe@company.com",
            phoneNumber = "0888111222"
        )
        // when
        val person = personDTO.convertToPerson()
        // then
        assertNull(person.id)
        assertEquals("John Doe", person.name)
        assertEquals("john.doe@company.com", person.email)
        assertEquals("0888111222", person.phoneNumber)
    }

    @Test
    fun `should convert PersonDTO with nulls to Person`() {
        // given
        val personDTO = PersonDTO(
            name = "John Doe",
        )
        // when
        val person = personDTO.convertToPerson()
        // then
        assertEquals(personDTO.name, person.name)
        assertNull(person.id)
        assertNull(person.email)
        assertNull(person.phoneNumber)
    }

    @Test
    fun `should convert Person to PersonDTO`() {
        // given
        val person = Person(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe",
            email = "john.doe@comnany.com",
            phoneNumber = "0888111222"
        )
        // when
        val personDTO = person.convertToPersonDTO()
        // then
        assertEquals(person.id, personDTO.id)
        assertEquals(person.name, personDTO.name)
        assertEquals(person.email, personDTO.email)
        assertEquals(person.phoneNumber, personDTO.phoneNumber)
    }

    @Test
    fun `should merge PersonDTO to Person`() {
        // given
        val personDTO = PersonDTO(
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John Doe",
            email = "john.doe@comnany.com",
            phoneNumber = "0888111222"
        )
        val person = Person(
            id = personDTO.id,
            name = "John...",
            email = null,
            phoneNumber = null
        )
        // when
        personDTO.mergeToPerson(person)
        // then
        with(person) {
            assertEquals(personDTO.id, id)
            assertEquals(personDTO.name, name)
            assertEquals(personDTO.email, email)
            assertEquals(personDTO.phoneNumber, phoneNumber)
        }
    }
}
