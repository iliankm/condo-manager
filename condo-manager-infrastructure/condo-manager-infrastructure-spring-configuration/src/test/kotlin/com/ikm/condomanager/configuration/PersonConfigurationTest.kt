package com.ikm.condomanager.configuration

import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.SavePersonUseCase
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

    @Autowired
    lateinit var savePersonUseCase: SavePersonUseCase

    @Test
    fun `should configure LoadPersonUseCase`() {
        assertNotNull(loadPersonUseCase)
    }

    @Test
    fun `should configure SavePersonUseCase`() {
        assertNotNull(savePersonUseCase)
    }
}
