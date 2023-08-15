package com.ikm.condomanager.usecase.person

import com.ikm.condomanager.domain.Person

/**
 * Use case for creating a [Person] in the db.
 */
interface CreatePersonUseCase {
    /**
     * Creates the given valid [person] in the db.
     *
     * @param person the [Person] to be created.
     * @return the saved [Person].
     */
    fun create(person: Person): Person
}
