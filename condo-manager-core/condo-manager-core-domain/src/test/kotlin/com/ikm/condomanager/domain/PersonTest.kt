package com.ikm.condomanager.domain

import jakarta.validation.Validation
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID

/**
 * Unit test for [Person].
 */
class PersonTest {

    private val validator = Validation.buildDefaultValidatorFactory().validator

    @ParameterizedTest
    @MethodSource("validPersonParams")
    fun `should be a valid Person`(id: PersonId?, name: String, email: String?, phone: String?) {
        // given
        val person = Person(id = id, name = "")
        person.name = name
        person.email = email
        person.phoneNumber = phone
        // when
        val validations = validator.validate(person)
        // then
        assertEquals(0, validations.size)
        assertEquals(id, person.id)
        assertEquals(name, person.name)
        assertEquals(email, person.email)
        assertEquals(phone, person.phoneNumber)
    }

    @ParameterizedTest
    @MethodSource("invalidPersonParams")
    fun `should be a not valid Person`(person: Person, propertyToMessage: Pair<String, String>) {
        // when
        val validations = validator.validate(person)
        // then
        kotlin.test.assertTrue(
            validations.map { it.propertyPath.toString() to it.messageTemplate }.contains(propertyToMessage),
            "Actual validations: $validations"
        )
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
                    Person(
                        id = PersonId("", 1),
                        name = "John Doe",
                        email = "john.doe@someorg.com",
                        phoneNumber = "0888111222"
                    ),
                    "id.id" to "{jakarta.validation.constraints.NotBlank.message}"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = "",
                        email = "john.doe@someorg.com",
                        phoneNumber = "0888111222"
                    ),
                    "name" to "{jakarta.validation.constraints.NotBlank.message}"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = randomAlphabetic(71),
                        email = "john.doe@someorg.com",
                        phoneNumber = "0888111222"
                    ),
                    "name" to "{jakarta.validation.constraints.Size.message}"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = randomAlphabetic(70),
                        email = "aa",
                        phoneNumber = "0888111222"
                    ),
                    "email" to "{jakarta.validation.constraints.Email.message}"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = randomAlphabetic(71),
                        email = "aa@aa",
                        phoneNumber = "0888111222"
                    ),
                    "email" to "{jakarta.validation.constraints.Email.message}"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = randomAlphabetic(70),
                        email = "aa@aa.org",
                        phoneNumber = "123"
                    ),
                    "phoneNumber" to "Must be a valid phone number"
                ),
                Arguments.of(
                    Person(
                        id = PersonId(UUID.randomUUID().toString(), 1),
                        name = randomAlphabetic(70),
                        email = "aa@aa.org",
                        phoneNumber = "0888123412344445555555577777777777777777"
                    ),
                    "phoneNumber" to "Must be a valid phone number"
                )
            )
    }
}
