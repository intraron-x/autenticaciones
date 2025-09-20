package com.intraron.r2dbc.entity;

import com.intraron.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// intraron: Esta clase es la entidad de persistencia. Se usa para interactuar
// con la base de datos y, como está en la capa de infraestructura,
// puede implementar la interfaz de Spring Security.

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("userdata")
public class UserEntity implements UserDetails {

    @Column("nombres") // Mapea 'nombres' a 'NOMBRES'
    private String nombres;
    @Column("apellidos") // Mapea 'apellidos' a 'APELLIDOS'
    private String apellidos;
    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento; // Se usa LocalDate para la fecha
    @Column("direccion") // Mapea 'direccion' a 'DIRECCION'
    private String direccion;
    @Column("telefono") // Mapea 'telefono' a 'TELEFONO'
    private String telefono;
    @Column("correo_electronico") // Mapea 'correoElectronico' a 'CORREO_ELECTRONICO'
    private String correoElectronico;
    @Column("salario_base") // Mapea 'salarioBase' a 'SALARIO_BASE'
    private Double salarioBase;
    @Column("password")
    private String password; // intraron: Nuevo campo para la contraseña
    @Column("roles")
    private List<String> roles;
    @Id
    private String id;

    // intraron: Mapeo de la entidad de persistencia al modelo de dominio.
    // Ahora convertimos List<String> a Set<String>.
    public User toUser() {
        return User.builder()
                .nombres(this.nombres)
                .apellidos(this.apellidos)
                .telefono(this.telefono)
                .direccion(this.direccion)
                .correoElectronico(this.correoElectronico)
                .password(this.password)
                .salarioBase(this.salarioBase)
                .roles(this.roles != null ?
                        Set.copyOf(this.roles) :
                        Set.of())
                .id(this.id)
                .build();
    }

    // intraron: La implementación de UserDetails ahora usa el List de roles.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.roles == null || this.roles.isEmpty()) {
            return Set.of();
        }
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.correoElectronico;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
