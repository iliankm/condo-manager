package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.usecase.person.CreatePersonUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [CreatePersonUseCase].
 */
@Service
class CreatePersonService(
    private val createPersonPort: CreatePersonPort
) : CreatePersonUseCase {
    override fun create(person: Person) =
        createPersonPort.create(person)
}
