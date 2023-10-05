package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import kotlin.test.assertNull

/**
 * Unit test for [Person].
 */
class PersonTest {

    @Test
    fun `should create Person`() {
        // given
        val person = Person.create(
            name = "John Doe",
            email = "john.doe@company.com",
            phoneNumber = "00888111222"
        )
        // then
        with(person) {
            assertEquals(name, "John Doe")
            assertNull(id)
            assertEquals("john.doe@company.com", email)
            assertEquals("00888111222", phoneNumber)
            assertEquals(
                "Person(id=null, name='John Doe', email=john.doe@company.com, phoneNumber=00888111222)",
                person.toString()
            )
        }
    }

    @Test
    fun `should modify Person`() {
        // given
        val id = PersonId(UUID.randomUUID().toString(), 1)
        val person = Person(id, "John D.", "john.d@company.com", "00888111111")
        // when
        person.name = "John Doe"
        person.email = "john.doe@company.com"
        person.phoneNumber = "00888111222"
        person.validate()
        // then
        with(person) {
            assertEquals("John Doe", name)
            assertEquals(id, this.id)
            assertEquals("john.doe@company.com", email)
            assertEquals("00888111222", phoneNumber)
            assertEquals(
                "Person(id=$id, name='John Doe', email=john.doe@company.com, phoneNumber=00888111222)",
                person.toString()
            )
        }
    }

    @ParameterizedTest
    @MethodSource("validPersonParams")
    fun `should be a valid Person`(id: PersonId?, name: String, email: String?, phone: String?) {
        // when
        val person = Person(id = id, name = name, email = email, phoneNumber = phone)
        // then
        assertEquals(id, person.id)
        assertEquals(name, person.name)
        assertEquals(email, person.email)
        assertEquals(phone, person.phoneNumber)
    }

    @ParameterizedTest
    @MethodSource("invalidPersonParams")
    fun `should be a not valid Person`(
        id: PersonId?,
        name: String,
        email: String?,
        phone: String?,
        propertyToMessage: Pair<String, String>
    ) {
        // when
        val exception = assertThrows(ConstraintViolationException::class.java) {
            Person(id = id, name = name, email = email, phoneNumber = phone)
        }
        // then
        exception.assert(propertyToMessage)
    }

    companion object {
        @JvmStatic
        fun validPersonParams() =
            listOf(
                Arguments.of(
                    null,
                    "John Doe",
                    null,
                    null
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    "John Doe",
                    null,
                    null
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    "John Doe",
                    "john.doe@some-org.com",
                    "0888111222"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 2),
                    randomAlphabetic(70),
                    "some.one@some.org.com",
                    "0888111222"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 3),
                    randomAlphabetic(70),
                    "someone@something.com",
                    "0888-111-222"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 4),
                    randomAlphabetic(70),
                    "someone@something.com",
                    "0888 111 222"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 5),
                    randomAlphabetic(70),
                    "someone@something.com",
                    "+359894991153"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 6),
                    randomAlphabetic(70),
                    "someone@something.com",
                    "+359 894 991 153"
                )
            )

        @JvmStatic
        @Suppress("LongMethod")
        fun invalidPersonParams() =
            listOf(
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    "",
                    "john.doe@someorg.com",
                    "0888111222",
                    "name" to "{jakarta.validation.constraints.NotBlank.message}"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    randomAlphabetic(71),
                    "john.doe@someorg.com",
                    "0888111222",
                    "name" to "{jakarta.validation.constraints.Size.message}"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    randomAlphabetic(70),
                    "aa",
                    "0888111222",
                    "email" to "{jakarta.validation.constraints.Email.message}"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    randomAlphabetic(71),
                    "aa@aa",
                    "0888111222",
                    "email" to "{jakarta.validation.constraints.Email.message}"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    randomAlphabetic(70),
                    "aa@aa.org",
                    "123",
                    "phoneNumber" to "Must be a valid phone number"
                ),
                Arguments.of(
                    PersonId(UUID.randomUUID().toString(), 1),
                    randomAlphabetic(70),
                    "aa@aa.org",
                    "0888123412344445555555577777777777777777",
                    "phoneNumber" to "Must be a valid phone number"
                )
            )
    }
}
