package com.ikm.condomanager.exception

/**
 * Exception to be thrown in case given object cannot be found in the db.
 */
class NotFoundException(message: String) : RuntimeException(message)
