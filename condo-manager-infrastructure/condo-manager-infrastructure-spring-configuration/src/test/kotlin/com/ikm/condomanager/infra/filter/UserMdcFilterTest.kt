package com.ikm.condomanager.infra.filter

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyAll
import jakarta.servlet.DispatcherType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.slf4j.MDC
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Unit test for [UserMdcFilter].
 */
class UserMdcFilterTest {

    private val filter: OncePerRequestFilter = UserMdcFilter()

    @ParameterizedTest
    @MethodSource("doFilterInternalTestParameters")
    fun doFilterInternalTest(auth: Authentication?, expectedUsername: String?) {
        // given
        mockkStatic(MDC::put, MDC::remove)
        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val filterChain = mockk<FilterChain>()
        every { request.getAttribute(any()) } returns null
        every { request.dispatcherType } returns DispatcherType.REQUEST
        every { request.setAttribute(any(), any()) } returns Unit
        every { request.removeAttribute(any()) } returns Unit
        every { filterChain.doFilter(request, response) } returns Unit
        every { MDC.put("user", "my-user") } returns Unit
        every { MDC.remove("user") } returns Unit
        SecurityContextHolder.getContext().authentication = auth

        // when
        filter.doFilter(request, response, filterChain)
        // then
        verifyAll {
            MDC.put("user", expectedUsername)
            MDC.remove("user")
            filterChain.doFilter(request, response)
        }
    }

    companion object {
        @JvmStatic
        fun doFilterInternalTestParameters() =
            listOf(
                Arguments.of(TestingAuthenticationToken(User("my-user", "", emptySet()), ""), "my-user"),
                Arguments.of(null, null)
            )
    }
}
