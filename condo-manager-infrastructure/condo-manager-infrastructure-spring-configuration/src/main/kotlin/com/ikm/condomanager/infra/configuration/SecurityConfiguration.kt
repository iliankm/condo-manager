package com.ikm.condomanager.infra.configuration

import com.ikm.condomanager.domain.Role.CONDO_MANAGER_USER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring configuration for web security.
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            // No sessions
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // Headers
            .headers { headers ->
                // Disable response caching
                headers.cacheControl { cache ->
                    cache.disable()
                }
            }
            // Disable csrf
            .csrf { csrf ->
                csrf.disable()
            }
            // OAuth2 resource server
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { }
            }
            // Endpoints authorization
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                auth.requestMatchers("/api/**").hasRole(CONDO_MANAGER_USER.value)
            }
            .build()

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles")
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_")

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username")
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)

        return jwtAuthenticationConverter
    }
}
