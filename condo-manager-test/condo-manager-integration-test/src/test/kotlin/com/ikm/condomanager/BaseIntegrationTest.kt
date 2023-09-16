package com.ikm.condomanager

import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

/**
 * Base class for integration tests.
 */
sealed class BaseIntegrationTest {
    companion object {
        private const val POSTGRE_SQL_PORT = 5432
        private const val CONDO_MANAGER_APP_PORT = 8080
        private const val ENV_IMAGE_TAG = "IMAGE_TAG"
        private const val POSTGRE_SQL_SERVICE_NAME = "postgres_1"
        private const val CONDO_MANAGER_APP_SERVICE_NAME = "condo-manager_1"

        @JvmStatic
        private val environment: DockerComposeContainer<*> =
            DockerComposeContainer(File("../../docker/docker-compose-integration-test.yml"))
                .withExposedService(
                    POSTGRE_SQL_SERVICE_NAME,
                    POSTGRE_SQL_PORT,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30))
                )
                .withExposedService(
                    CONDO_MANAGER_APP_SERVICE_NAME,
                    CONDO_MANAGER_APP_PORT,
                    Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30))
                )
                .withEnv(ENV_IMAGE_TAG, System.getProperty(ENV_IMAGE_TAG))
                .withLocalCompose(true)

        init {
            environment.start()
        }
    }

    private val applicationHost =
        environment.getServiceHost(CONDO_MANAGER_APP_SERVICE_NAME, CONDO_MANAGER_APP_PORT)

    private val applicationPort =
        environment.getServicePort(CONDO_MANAGER_APP_SERVICE_NAME, CONDO_MANAGER_APP_PORT)

    @BeforeEach
    internal fun init() {
        RestAssured.baseURI = "http://$applicationHost"
        RestAssured.port = applicationPort
    }
}
