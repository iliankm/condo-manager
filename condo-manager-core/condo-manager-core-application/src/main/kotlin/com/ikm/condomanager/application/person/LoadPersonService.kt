package com.ikm.condomanager.application.person

import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ikm.condomanager.usecase.person.LoadPersonUseCase

/**
 * Service implementation of [LoadPersonUseCase].
 */
class LoadPersonService(
    private val loadPersonPort: LoadPersonPort
) : LoadPersonUseCase {
    override fun load(id: PersonId) =
        loadPersonPort.load(id)
}
