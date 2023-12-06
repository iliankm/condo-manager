package com.ikm.condomanager.domain

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validation
import jakarta.validation.Validator

/**
 * Abstract class for self-validating domain objects.
 */
interface SelfValidating {
    /**
     * Validates the current instance.
     */
    fun validate() =
        validator.validate(this).takeIf { it.isNotEmpty() }?.let { throw ConstraintViolationException(it) }
}

private val validator: Validator = Validation.buildDefaultValidatorFactory().validator
