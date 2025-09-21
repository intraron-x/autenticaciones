/**
 * @author intraron
 * Esta clase define las rutas para los endpoints relacionados con los usuarios.
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

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRouter {

    @Bean("userRoutes")
    @Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
    @RouterOperations({
            // Endpoint: GET /api/v1/users
            @RouterOperation(
                    path = "/api/v1/users",
                    operation = @Operation(
                            summary = "Obtener todos los usuarios",
                            description = "Devuelve una lista de todos los usuarios registrados",
                            operationId = "getAllUsers",
                            tags = {"Usuarios"}
                    )
            ),
            // Endpoint: GET /api/v1/users/by-email
            @RouterOperation(
                    path = "/api/v1/users/by-email",
                    operation = @Operation(
                            summary = "Obtener un usuario por email",
                            description = "Busca y devuelve un usuario específico usando su dirección de email",
                            operationId = "getUserByEmail",
                            tags = {"Usuarios"}
                    )
            )
    })
    public RouterFunction<ServerResponse> route(UserHandler userHandler) {
        return RouterFunctions.route(GET("/api/v1/users").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), userHandler::getAllUsers)
                // intraron: Se agrega una nueva ruta para la consulta interna por email.
                .andRoute(GET("/api/v1/users/by-email").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), userHandler::getUserByEmail);
    }
}
