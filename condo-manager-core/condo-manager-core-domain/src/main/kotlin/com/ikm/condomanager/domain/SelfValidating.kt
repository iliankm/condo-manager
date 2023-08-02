package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator

/**
 * Abstract class for self-validating domain objects.
 *
 * @param T the type parameter of the validated object.
 */
sealed class SelfValidating<T> {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    /**
     * Validates the current instance.
     */
    @Suppress("UNCHECKED_CAST")
    fun validate() =
        validator.validate(this as T).takeIf { it.isNotEmpty() }?.let { throw ConstraintViolationException(it) }
}
