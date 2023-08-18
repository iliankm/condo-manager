package com.ikm.condomanager.application.service.person

import com.ikm.condomanager.domain.PersonId
import com.ikm.condomanager.port.person.DeletePersonPort
import com.ikm.condomanager.usecase.person.DeletePersonUseCase
import org.springframework.stereotype.Service

/**
 * Service implementation of [DeletePersonUseCase].
 */
@Service
class DeletePersonService(
    val deletePersonPort: DeletePersonPort
) : DeletePersonUseCase {
    override fun delete(personId: PersonId) {
        deletePersonPort.delete(personId)
    }
}
