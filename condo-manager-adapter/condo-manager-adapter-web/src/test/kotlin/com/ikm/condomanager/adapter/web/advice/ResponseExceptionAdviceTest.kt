package com.ikm.condomanager.adapter.web.advice

import com.ikm.condomanager.adapter.web.controller.BaseControllerTest
import com.ikm.condomanager.exception.NotFoundException
import com.ikm.condomanager.exception.VersionNotMatchedException
import jakarta.validation.ConstraintViolationException
import org.hibernate.validator.internal.engine.ConstraintViolationImpl
import org.hibernate.validator.internal.engine.path.PathImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.PRECONDITION_FAILED
import org.springframework.http.ProblemDetail
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Spring test for [ResponseExceptionAdvice].
 */
@ContextConfiguration(classes = [ResponseExceptionAdvice::class, ResponseExceptionAdviceTestController::class])
class ResponseExceptionAdviceTest : BaseControllerTest() {
    private val responseExceptionAdvice = ResponseExceptionAdvice()

    @Test
    fun `given NotFoundException should return ResponseEntity for 404 HTTP code`() {
        // given
        val ex = NotFoundException("message")
        // when
        val result = responseExceptionAdvice.handleNotFoundException(ex)
        // then
        assertEquals(NOT_FOUND, result.statusCode)
        assertEquals(ProblemDetail.forStatusAndDetail(NOT_FOUND, "message"), result.body)
    }

    @Test
    fun `should return 404 HTTP code`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/nfe").with(jwt())
        )
        // then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `given VersionNotMatchedException should return ResponseEntity for 412 HTTP code`() {
        // given
        val ex = VersionNotMatchedException("message")
        // when
        val result = responseExceptionAdvice.handleVersionNotMatchedException(ex)
        // then
        assertEquals(PRECONDITION_FAILED, result.statusCode)
        assertEquals(ProblemDetail.forStatusAndDetail(PRECONDITION_FAILED, "message"), result.body)
    }

    @Test
    fun `should return 412 HTTP code`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/vne").with(jwt())
        )
        // then
        result.andExpect(status().isPreconditionFailed)
    }

    @Test
    fun `given ConstraintViolationException should return ResponseEntity for 400 HTTP code`() {
        // given
        val ex = ConstraintViolationException(
            setOf(
                ConstraintViolationImpl.forBeanValidation(
                    null,
                    null,
                    null,
                    "message",
                    ResponseExceptionAdviceTest::class.java,
                    null,
                    null,
                    null,
                    PathImpl.createPathFromString("prop"),
                    null,
                    null
                )
            )
        )
        // when
        val result = responseExceptionAdvice.handleValidationException(ex)
        // then
        assertEquals(BAD_REQUEST, result.statusCode)
        assertEquals(
            ProblemDetail.forStatusAndDetail(
                BAD_REQUEST,
                "ResponseExceptionAdviceTest#prop message"
            ),
            result.body
        )
    }

    @Test
    fun `should return 400 HTTP code`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/cve").with(jwt())
        )
        // then
        result.andExpect(status().isBadRequest)
    }
}

@RestController
@RequestMapping("api/v1/test")
internal class ResponseExceptionAdviceTestController {
    @GetMapping("nfe")
    fun throwNotFoundException(): Nothing =
        throw NotFoundException("NFE")

    @GetMapping("vne")
    fun throwVersionNotMatchedException(): Nothing =
        throw VersionNotMatchedException("VNE")

    @GetMapping("cve")
    fun throwConstraintViolationException(): Nothing =
        throw ConstraintViolationException(emptySet())
}
