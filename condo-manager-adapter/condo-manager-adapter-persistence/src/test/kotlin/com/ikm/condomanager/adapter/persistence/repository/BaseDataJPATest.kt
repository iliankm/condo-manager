package com.ikm.condomanager.adapter.persistence.repository

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer

/**
 * Base class for spring data JPA tests.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test-persistence")
sealed class BaseDataJPATest {
    companion object {
        private const val POSTGRE_SQL_IMAGE = "postgres:15.3"
        private const val POSTGRE_SQL_DB_NAME = "condo-manager"
        private const val POSTGRE_SQL_USERNAME = "postgres"
        private const val POSTGRE_SQL_PASSWORD = "postgres"

        @JvmStatic
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer(POSTGRE_SQL_IMAGE)
            .withDatabaseName(POSTGRE_SQL_DB_NAME)
            .withUsername(POSTGRE_SQL_USERNAME)
            .withPassword(POSTGRE_SQL_PASSWORD)

        init {
            postgreSQLContainer.start()
            System.setProperty("DB_URL", postgreSQLContainer.getJdbcUrl())
            System.setProperty("DB_USERNAME", postgreSQLContainer.username)
            System.setProperty("DB_PASSWORD", postgreSQLContainer.password)
        }
    }
}
