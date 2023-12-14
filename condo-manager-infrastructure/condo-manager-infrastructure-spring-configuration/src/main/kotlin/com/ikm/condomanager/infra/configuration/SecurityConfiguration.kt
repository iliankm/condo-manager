package com.ikm.condomanager.infra.configuration

import com.ikm.condomanager.domain.Role.CONDO_MANAGER_USER
import com.ikm.condomanager.domain.Role.MONITORING
import com.ikm.condomanager.infra.filter.UserMdcFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

/**
 * Spring configuration for web security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
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
            // HTTP Basic authentication for the actuator endpoints
            .httpBasic(Customizer.withDefaults())
            // OAuth2 resource server
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { }
            }
            // Endpoints authorization
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/swagger-ui/**").permitAll()
                auth.requestMatchers("/api-docs/**").permitAll()
                auth.requestMatchers("/actuator/health").permitAll()
                auth.requestMatchers("/actuator/info").permitAll()
                auth.requestMatchers("/actuator/**").hasRole(MONITORING.value)
                auth.requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                auth.requestMatchers("/api/**").hasRole(CONDO_MANAGER_USER.value)
            }
            .addFilterAfter(UserMdcFilter(), AuthorizationFilter::class.java)
            .build()

    @Bean
    fun jwtGrantedAuthoritiesConverter(): JwtGrantedAuthoritiesConverter {
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles")
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_")

        return jwtGrantedAuthoritiesConverter
    }

    @Bean
    fun jwtAuthenticationConverter(
        jwtGrantedAuthoritiesConverter: JwtGrantedAuthoritiesConverter
    ): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username")
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)

        return jwtAuthenticationConverter
    }

    @Bean
    fun userDetailsService(
        @Value("\${management.endpoints.security.username}") monitoringUsername: String,
        @Value("\${management.endpoints.security.password}") monitoringPassword: String
    ): UserDetailsService {
        val monitoringUser = User.builder()
            .username(monitoringUsername)
            .password("{noop}$monitoringPassword")
            .roles(MONITORING.value)
            .build()
        return InMemoryUserDetailsManager(monitoringUser)
    }
}

val Authentication.username: String?
    get() = when (this) {
        is JwtAuthenticationToken -> name
        else -> (principal as? UserDetails)?.username
    }
