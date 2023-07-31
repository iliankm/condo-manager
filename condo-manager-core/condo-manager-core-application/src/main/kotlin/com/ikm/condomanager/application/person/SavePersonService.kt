package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.usecase.person.SavePersonUseCase

/**
 * Service implementation of [SavePersonUseCase].
 */
class SavePersonService : SavePersonUseCase {
    override fun save(person: Person): Person {
        TODO("Not yet implemented")
    }
}
