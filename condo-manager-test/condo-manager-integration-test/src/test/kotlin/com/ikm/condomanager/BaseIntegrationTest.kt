package com.ikm.condomanager

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.apache.http.HttpHeaders.AUTHORIZATION
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration.ofSeconds
import java.util.Base64

/**
 * Base class for integration tests.
 */
sealed class BaseIntegrationTest {
    companion object {
        private const val CONDO_MANAGER_APP_PORT = 8080
        private const val ENV_IMAGE_TAG = "IMAGE_TAG"
        private const val KEYCLOAK_PORT = 8081
        private const val KEYCLOAK_SERVICE_NAME = "keycloak-it_1"
        private const val CONDO_MANAGER_APP_SERVICE_NAME = "condo-manager-it_1"

        @JvmStatic
        protected val environment: DockerComposeContainer<*> =
            DockerComposeContainer(File("../../docker/docker-compose-integration-test.yml"))
                .withExposedService(
                    KEYCLOAK_SERVICE_NAME,
                    KEYCLOAK_PORT,
                    Wait.forListeningPort().withStartupTimeout(ofSeconds(60))
                )
                .withExposedService(
                    CONDO_MANAGER_APP_SERVICE_NAME,
                    CONDO_MANAGER_APP_PORT,
                    Wait.forListeningPort().withStartupTimeout(ofSeconds(60))
                )
                .withEnv(ENV_IMAGE_TAG, System.getProperty(ENV_IMAGE_TAG))
                .withLocalCompose(true)

        @JvmStatic
        protected val applicationHost: String

        @JvmStatic
        protected val applicationPort: Int

        @JvmStatic
        protected val keycloakHost: String

        @JvmStatic
        protected val keycloakPort: Int

        @JvmStatic
        protected val keycloak: Keycloak

        init {
            environment.start()

            applicationHost = environment.getServiceHost(CONDO_MANAGER_APP_SERVICE_NAME, CONDO_MANAGER_APP_PORT)
            applicationPort = environment.getServicePort(CONDO_MANAGER_APP_SERVICE_NAME, CONDO_MANAGER_APP_PORT)
            keycloakHost = environment.getServiceHost(KEYCLOAK_SERVICE_NAME, KEYCLOAK_PORT)
            keycloakPort = environment.getServicePort(KEYCLOAK_SERVICE_NAME, KEYCLOAK_PORT)
            keycloak = Keycloak("http://$keycloakHost:$keycloakPort")
        }
    }

    @BeforeEach
    internal fun init() {
        RestAssured.baseURI = "http://$applicationHost"
        RestAssured.port = applicationPort
    }
}

internal fun RequestSpecification.basicAuth(username: String, password: String): RequestSpecification {
    val auth = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
    header(AUTHORIZATION, "Basic $auth")
    return this
}

internal fun RequestSpecification.oauth2(accessToken: String?): RequestSpecification {
    if (!accessToken.isNullOrEmpty()) {
        header(AUTHORIZATION, "Bearer $accessToken")
    }
    return this
}
