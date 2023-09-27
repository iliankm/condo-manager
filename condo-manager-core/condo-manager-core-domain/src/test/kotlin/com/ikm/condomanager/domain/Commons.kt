package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import kotlin.test.assertTrue

/**
 * Assert [ConstraintViolationException] constraint violations for presence of a message for a given property.
 *
 * @param propertyToMessage property name to message template.
 */
internal fun ConstraintViolationException.assert(propertyToMessage: Pair<String, String>) {
    assertTrue(
        constraintViolations
            .map { it.propertyPath.toString() to it.messageTemplate }.contains(propertyToMessage),
        """
            Expected property path to message: $propertyToMessage 
            Actual validations: $constraintViolations
        """.trimIndent()
    )
}
