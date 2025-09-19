/**
 * @author intraron
 * Esta clase maneja las solicitudes relacionadas con usuarios (no de autenticación).
 */
package com.intraron.api;

import com.intraron.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers(), Object.class);
    }

    /**
     * @author intraron
     * Maneja la solicitud para obtener un usuario por su correo electrónico.
     * Este endpoint está diseñado para ser consumido internamente por otros microservicios.
     * @param serverRequest La solicitud del servidor.
     * @return Mono<ServerResponse> con el usuario encontrado o un error si no existe.
     */
    public Mono<ServerResponse> getUserByEmail(ServerRequest serverRequest) {
        String email = serverRequest.queryParam("email")
                .orElseThrow(() -> new IllegalArgumentException("El parámetro 'email' es requerido."));

        log.info("Consulta interna para buscar usuario con email: {}", email);

        return userUseCase.findByCorreoElectronico(email)
                .flatMap(user -> ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }
}
