package com.why.studio.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@DynamicInsert
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @ColumnDefault("''")
    private String firstName;
    @Column(nullable = false)
    @ColumnDefault("''")
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @ColumnDefault("''")
    private String phone;
    @Column(nullable = false)
    @ColumnDefault("''")
    private String password;
    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

}
