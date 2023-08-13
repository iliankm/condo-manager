package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.LoadPersonPort
import com.ikm.condomanager.usecase.person.LoadPersonUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [LoadPersonUseCase].
 */
@Service
class LoadPersonService(
    private val loadPersonPort: LoadPersonPort
) : LoadPersonUseCase {
    override fun load(id: PersonId) =
        loadPersonPort.load(id)
}
