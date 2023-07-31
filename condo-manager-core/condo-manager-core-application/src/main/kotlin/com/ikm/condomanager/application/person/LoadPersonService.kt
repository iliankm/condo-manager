package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.usecase.person.LoadPersonUseCase

/**
 * Service implementation of [LoadPersonUseCase].
 */
class LoadPersonService : LoadPersonUseCase {
    override fun load(id: PersonId): Person {
        TODO("Not yet implemented")
    }
}
