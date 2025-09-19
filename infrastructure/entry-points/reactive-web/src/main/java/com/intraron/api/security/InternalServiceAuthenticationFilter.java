// Archivo: com/intraron/api/security/InternalServiceAuthenticationFilter.java
/**
 * @author intraron
 * Filtro de seguridad para autenticar llamadas de servicio a servicio usando un API Key.
 * Actúa como una capa de seguridad para endpoints internos, como la consulta de usuarios
 * desde el microservicio de solicitudes.
 */
package com.intraron.api.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


import java.util.Collections;

@Slf4j
@Component
public class InternalServiceAuthenticationFilter implements WebFilter {

    @Value("${intraron.internal.service-api-key:default-secret-key}")
    private String serviceApiKey;

    private static final String API_KEY_HEADER = "X-API-Key";
    // intraron: Se inyecta la clave API secreta desde la configuración.
    private final String internalServiceApiKey;
    public InternalServiceAuthenticationFilter(@Value("${intraron.internal.service-api-key}") String internalServiceApiKey) {
        this.internalServiceApiKey = internalServiceApiKey;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // intraron: Se obtiene el API Key del encabezado de la solicitud.
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");
        String path = exchange.getRequest().getURI().getPath();

        // intraron: Se verifica si el API Key es el correcto y si la ruta es la que debe ser validada por este filtro.
        if (apiKey != null && apiKey.equals(serviceApiKey) && path.contains("/api/v1/users/by-email")) {
            log.info("Solicitud interna de servicio autenticada exitosamente.");
            // intraron: Se crea un objeto de autenticación con el rol especial.
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "internal-service", // Nombre del servicio.
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE_TO_SERVICE_COMMUNICATION")) // Rol para la comunicación interna.
            );
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        // intraron: Si no es una solicitud de servicio, se continúa con la cadena de filtros normal (por ejemplo, el JWT).
        return chain.filter(exchange);
    }

}