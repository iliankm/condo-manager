package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ikm.condomanager.usecase.person.SavePersonUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [SavePersonUseCase].
 */
@Service
class SavePersonService(
    private val createPersonPort: CreatePersonPort,
    private val updatePersonPort: UpdatePersonPort
) : SavePersonUseCase {
    override fun save(person: Person): Person =
        if (person.id == null) {
            createPersonPort.create(person)
        } else {
            updatePersonPort.update(person)
        }
}
