package com.ikm.condomanager.adapter.web.advice

import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException
import jakarta.validation.ConstraintViolationException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.PRECONDITION_FAILED
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Web MVC response exception handler responsible for mapping specific exceptions thrown by the application
 * to appropriate HTTP response codes.
 */
@ControllerAdvice
class ResponseExceptionAdvice : ResponseEntityExceptionHandler() {

    /**
     * Handler for [NotFoundException] returning HTTP 404 response code.
     */
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException) =
        ResponseEntity.status(NOT_FOUND).body(ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.message.orEmpty()))

    /**
     * Handler for [VersionNotMatchedException] returning HTTP 412 response code.
     */
    @ExceptionHandler(VersionNotMatchedException::class)
    fun handleVersionNotMatchedException(ex: VersionNotMatchedException) =
        ResponseEntity.status(PRECONDITION_FAILED)
            .body(ProblemDetail.forStatusAndDetail(PRECONDITION_FAILED, ex.message.orEmpty()))

    /**
     * Handler for [ValidationException] returning HTTP 400 response code.
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException) =
        ResponseEntity.status(BAD_REQUEST)
            .body(
                ProblemDetail.forStatusAndDetail(
                    BAD_REQUEST,
                    ex.constraintViolations.joinToString {
                        "${it.rootBeanClass.simpleName}#${it.propertyPath} ${it.message}"
                    }
                )
            )
}
