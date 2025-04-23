package com.infsis.socialpagebackend.authentication.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "rol_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id_permission")
    )
    private Set<Permissions> permisos;
}
