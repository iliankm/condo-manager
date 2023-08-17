package com.ikm.condomanager.adapter.web.converter

import com.ikm.condomanager.adapter.web.controller.PersonDTO
import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
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
        assertEquals(personDTO.id, person.id)
        assertEquals(personDTO.name, person.name)
        assertEquals(personDTO.email, person.email)
        assertEquals(personDTO.phoneNumber, person.phoneNumber)
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
            id = PersonId(UUID.randomUUID().toString(), 0),
            name = "John..."
        )
        // when
        val mergedPerson = personDTO.mergeToPerson(person)
        // then
        assertSame(person, mergedPerson)
        assertEquals(person.id, mergedPerson.id)
        assertEquals(personDTO.name, mergedPerson.name)
        assertEquals(personDTO.email, mergedPerson.email)
        assertEquals(personDTO.phoneNumber, mergedPerson.phoneNumber)
    }
}
