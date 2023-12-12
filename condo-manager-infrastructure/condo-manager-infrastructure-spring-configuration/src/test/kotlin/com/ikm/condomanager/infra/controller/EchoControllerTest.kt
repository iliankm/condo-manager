package com.ikm.condomanager.infra.controller

import com.ikm.condomanager.domain.AuthenticatedUser
import com.ikm.condomanager.domain.Role
import com.ikm.condomanager.port.user.LoadCurrentUserPort
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verifyAll
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
@ContextConfiguration(classes = [EchoController::class, TestComponent::class])
@ActiveProfiles("integration-test")
class EchoControllerTest {
    @MockkBean
    lateinit var testComponent: TestComponent

    @MockkBean
    lateinit var loadCurrentUserPort: LoadCurrentUserPort

    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `should get ping`() {
        // given
        every { testComponent.test1() } returns Unit
        every { testComponent.test2() } returns Unit
        every { testComponent.test3() } returns Unit
        // when
        val result = mvc.perform(
            get("/api/v1/test/ping").with(jwt())
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(content().string("ping"))
        verifyAll {
            testComponent.test1()
            testComponent.test2()
            testComponent.test3()
        }
    }

    @Test
    fun `should get current user`() {
        // given
        every { loadCurrentUserPort.load() } returns AuthenticatedUser(
            username = "User",
            email = "user@domain.com",
            firstName = "John",
            lastName = "Doe",
            roles = setOf(Role.CONDO_MANAGER_USER)
        )
        // when
        val result = mvc.perform(
            get("/api/v1/test/user").with(jwt())
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(jsonPath("$.username").value("User"))
        verifyAll {
            loadCurrentUserPort.load()
        }
    }

    @Test
    fun `should get current user from MDC context`() {
        // given
        mockkStatic(MDC::get)
        every { MDC.get("user") } returns "User"
        // when
        val result = mvc.perform(
            get("/api/v1/test/user-in-mdc").with(jwt())
        )
        // then
        result.andExpect(status().isOk)
        result.andExpect(content().string("User"))
        verifyAll {
            MDC.get("user")
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
