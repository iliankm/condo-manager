package com.ikm.condomanager.infra.controller

import com.ikm.condomanager.domain.AuthenticatedUser
import com.ikm.condomanager.domain.Role.Names.CONDOMINIUM_READ
import com.ikm.condomanager.port.user.LoadCurrentUserPort
import jakarta.annotation.security.RolesAllowed
import org.slf4j.MDC
import org.springframework.context.annotation.Profile
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller used mainly for security configuration integration testing.
 * The endpoints and components are available only with active profile "integration-test".
 */
@Profile("integration-test")
@RestController
@RequestMapping("api/v1/test")
class EchoController(
    val testComponent: TestComponent,
    val loadCurrentUserPort: LoadCurrentUserPort
) {
    @GetMapping("{value}")
    fun echo(@PathVariable("value") value: String): String {
        testComponent.test1()
        testComponent.test2()
        testComponent.test3()
        return value
    }

    @GetMapping("user")
    fun getUser(): AuthenticatedUser? =
        loadCurrentUserPort.load()

    @GetMapping("user-in-mdc")
    fun getUserInMdc(): String =
        MDC.get("user")
}

/**
 * Test component available only with active profile "integration-test".
 */
@Profile("integration-test")
@Component
class TestComponent {
    @RolesAllowed(CONDOMINIUM_READ)
    fun test1() = Unit

    @Secured("ROLE_$CONDOMINIUM_READ")
    fun test2() = Unit

    @PreAuthorize("hasRole('$CONDOMINIUM_READ')")
    fun test3() = Unit
}
