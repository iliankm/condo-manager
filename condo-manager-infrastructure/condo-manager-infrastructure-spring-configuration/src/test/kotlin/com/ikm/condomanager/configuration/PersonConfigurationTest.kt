package com.ikm.condomanager.configuration

import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.SavePersonUseCase
import com.ninjasquad.springmockk.MockkBean
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
    @MockkBean
    lateinit var loadPersonPort: LoadPersonPort

    @MockkBean
    lateinit var createPersonPort: CreatePersonPort

    @MockkBean
    lateinit var updatePersonPort: UpdatePersonPort

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
