package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ikm.condomanager.usecase.person.UpdatePersonUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [UpdatePersonUseCase].
 */
@Service
class UpdatePersonService(
    private val updatePersonPort: UpdatePersonPort
) : UpdatePersonUseCase {
    override fun update(person: Person) =
        updatePersonPort.update(person)
}
