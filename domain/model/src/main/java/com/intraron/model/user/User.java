/**
 * @author intraron
 * Esta clase representa el modelo de datos de un usuario en el dominio.
 * Contiene la información personal necesaria para el registro.
 */

package com.intraron.model.user;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String id;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Double salarioBase;
    private String password;
    private Set<String> roles;
}

