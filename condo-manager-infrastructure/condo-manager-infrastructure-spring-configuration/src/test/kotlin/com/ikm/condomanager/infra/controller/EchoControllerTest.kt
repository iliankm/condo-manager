package com.ikm.condomanager.infra.controller

import com.ikm.condomanager.domain.Role
import com.ikm.condomanager.infra.configuration.SecurityConfiguration
import com.ikm.condomanager.infra.user.LoadCurrentUserAdapter
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

/**
 * Spring WebMvc test for [EchoController].
 */
@WebMvcTest
@Import(SecurityConfiguration::class)
@ContextConfiguration(classes = [EchoController::class, TestComponent::class, LoadCurrentUserAdapter::class])
@ActiveProfiles("test-infra-spring")
class EchoControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `should get ping`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/ping").with(
                jwt().authorities(
                    SimpleGrantedAuthority("ROLE_${Role.CONDO_MANAGER_USER.value}"),
                    SimpleGrantedAuthority("ROLE_${Role.CONDOMINIUM_READ.value}")
                )
            )
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(content().string("ping"))
    }

    @Test
    fun `should get current user`() {
        // when
        val result = mvc.perform(
            get("/api/v1/test/user").with(
                jwt().authorities(
                    SimpleGrantedAuthority("ROLE_${Role.CONDO_MANAGER_USER.value}")
                )
            )
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(jsonPath("$.username").value("user"))
    }

    @Test
    fun `should get current user from MDC context`() {
        // given
        mockkStatic(MDC::put, MDC::get, MDC::remove)
        every { MDC.put("user", "user") } returns Unit
        every { MDC.get("user") } returns "user"
        every { MDC.remove("user") } returns Unit
        // when
        val result = mvc.perform(
            get("/api/v1/test/user-in-mdc").with(
                jwt().authorities(
                    SimpleGrantedAuthority("ROLE_${Role.CONDO_MANAGER_USER.value}")
                )
            )
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(content().string("user"))
        verifyAll {
            MDC.put("user", "user")
            MDC.get("user")
            MDC.remove("user")
        }
    }
}

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestComponent::class])
@ActiveProfiles("integration-test")
class TestComponentTest {

    @Autowired
    lateinit var testComponent: TestComponent

    @Test
    fun test1() {
        testComponent.test1()
    }

    @Test
    fun test2() {
        testComponent.test2()
    }

    @Test
    fun test3() {
        testComponent.test3()
    }
}
