package com.ikm.condomanager.adapter.persistence

import com.ikm.condomanager.adapter.persistence.repository.BaseRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Spring data persistence configuration.
 */
@Configuration
@EnableJpaRepositories(
    basePackages = ["com.ikm.condomanager.adapter.persistence.repository"],
    repositoryBaseClass = BaseRepositoryImpl::class
)
class PersistenceConfiguration
