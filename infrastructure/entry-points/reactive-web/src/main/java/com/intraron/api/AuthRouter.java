/**
 * @author intraron
 * Esta clase define las rutas para los endpoints de autenticación y registro.
 */
package com.intraron.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
@Tag(name = "Autenticación", description = "Operaciones de registro y login de usuarios")
public class AuthRouter {

    @Bean("authRoutes")
    @RouterOperations({
            // Endpoint: POST /api/v1/auth/register
            @RouterOperation(
                    path = "/api/v1/auth/register",
                    operation = @Operation(
                            summary = "Registrar un nuevo usuario",
                            description = "Crea una nueva cuenta de usuario en el sistema",
                            operationId = "register",
                            tags = {"Autenticación"}
                    )
            ),
            // Endpoint: POST /api/v1/auth/login
            @RouterOperation(
                    path = "/api/v1/auth/login",
                    operation = @Operation(
                            summary = "Iniciar sesión",
                            description = "Autentica a un usuario y devuelve un token de acceso",
                            operationId = "login",
                            tags = {"Autenticación"}
                    )
            )
    })
    public RouterFunction<ServerResponse> route(AuthHandler authHandler) {
        return RouterFunctions.route(POST("/api/v1/auth/register").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), authHandler::register)
                .andRoute(POST("/api/v1/auth/login").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), authHandler::login);
    }
}
