package com.ikm.condomanager.infra.filter

import com.ikm.condomanager.infra.configuration.username
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter for putting current logged user in the MDC context.
 */
class UserMdcFilter : OncePerRequestFilter() {
    companion object {
        private const val USER_KEY = "user"
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.put(USER_KEY, SecurityContextHolder.getContext().authentication?.username)

        try {
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(USER_KEY)
        }
    }
}
