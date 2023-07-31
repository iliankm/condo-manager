package com.ikm.condomanager.configuration

import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertNotNull

/**
 * Spring test for [PersonConfiguration].
 */
@ExtendWith(SpringExtension::class)
@Import(PersonConfiguration::class)
class PersonConfigurationTest {
    @Autowired
    lateinit var loadPersonUseCase: LoadPersonUseCase

    @Test
    fun `should configure LoadPersonUseCase`() {
        assertNotNull(loadPersonUseCase)
    }
}
