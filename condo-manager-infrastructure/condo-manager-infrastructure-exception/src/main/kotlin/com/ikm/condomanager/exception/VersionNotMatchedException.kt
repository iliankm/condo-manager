package com.ikm.condomanager.exception

/**
 * Exception to be thrown in cases when a given object's version is not matched.
 */
class VersionNotMatchedException(message: String) : RuntimeException(message)
