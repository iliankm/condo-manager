package com.ikm.condomanager.configuration

import com.ikm.condomanager.application.person.LoadPersonService
import com.ikm.condomanager.application.person.SavePersonService
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import com.ikm.condomanager.usecase.person.SavePersonUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Spring configuration for person related components.
 */
@Configuration
class PersonConfiguration {
    @Bean
    fun loadPersonUseCase(loadPersonPort: LoadPersonPort): LoadPersonUseCase =
        LoadPersonService(loadPersonPort)

    @Bean
    fun savePersonUseCase(
        createPersonPort: CreatePersonPort,
        updatePersonPort: UpdatePersonPort
    ): SavePersonUseCase =
        SavePersonService(createPersonPort, updatePersonPort)
}
