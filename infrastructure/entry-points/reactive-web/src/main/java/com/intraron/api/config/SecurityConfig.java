package com.intraron.api.config;

import com.intraron.api.security.InternalServiceAuthenticationFilter;
import com.intraron.api.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final InternalServiceAuthenticationFilter internalServiceAuthenticationFilter; // intraron: Nuevo filtro para comunicación entre servicios

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          ReactiveUserDetailsService reactiveUserDetailsService,
                          PasswordEncoder passwordEncoder,
                          InternalServiceAuthenticationFilter internalServiceAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.internalServiceAuthenticationFilter = internalServiceAuthenticationFilter;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // intraron: Deshabilitar CSRF para APIs REST
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // intraron: Deshabilitar autenticación HTTP básica
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // intraron: Deshabilitar formulario de login
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // intraron: Deshabilitar el almacenamiento del contexto de seguridad, es stateless con JWT
				.headers(ServerHttpSecurity.HeaderSpec::disable) // <- Esta es la línea que soluciona el problem
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/login", "/api/v1/auth/register", "/webjars/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // intraron: Permitir acceso a los endpoints de autenticación y documentación
                        // intraron: Se agrega una nueva regla para el endpoint de comunicación entre servicios.
                        // Solo permite el acceso si la solicitud ha sido autenticada como 'SERVICE_TO_SERVICE_COMMUNICATION'.
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/by-email").hasRole("SERVICE_TO_SERVICE_COMMUNICATION")
                        .anyExchange().authenticated() // intraron: Requerir autenticación para cualquier otra solicitud
                )
                // intraron: El nuevo filtro se añade antes del filtro JWT.
                .addFilterBefore(internalServiceAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION) // intraron: Añadir nuestro filtro JWT al inicio de la cadena de seguridad
                .build();
    }
}
