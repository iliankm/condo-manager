package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.port.person.CreatePersonPort
import com.ikm.condomanager.port.person.UpdatePersonPort
import com.ikm.condomanager.usecase.person.SavePersonUseCase

/**
 * Service implementation of [SavePersonUseCase].
 */
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
