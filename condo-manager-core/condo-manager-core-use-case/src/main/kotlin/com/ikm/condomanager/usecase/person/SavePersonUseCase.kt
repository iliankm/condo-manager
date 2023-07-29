package com.ikm.condomanager.usecase.person

import com.ikm.condomanager.domain.Person
import com.ikm.condomanager.exception.NotFoundException
import jakarta.validation.Valid
import jakarta.validation.ValidationException
import org.springframework.validation.annotation.Validated

/**
 * Use case for saving a [Person] in the db.
 */
@Validated
interface SavePersonUseCase {
    /**
     * Saves the given valid [person] in the db.
     * If [Person.id] is null, it's inserted in the db, otherwise it's updated.
     *
     * @param person the [Person] to be saved.
     * @return the saved [Person].
     * @throws ValidationException if the passed [person] is not valid.
     * @throws NotFoundException if the [Person] to be updated, identified by [Person.id], is not found in the db.
     */
    fun save(@Valid person: Person): Person
}
