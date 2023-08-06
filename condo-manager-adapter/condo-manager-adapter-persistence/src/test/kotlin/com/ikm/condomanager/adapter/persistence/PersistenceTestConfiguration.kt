package com.ikm.condomanager.adapter.persistence

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

/**
 * Spring data persistence configuration for tests.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class PersistenceTestConfiguration : PersistenceConfiguration()
