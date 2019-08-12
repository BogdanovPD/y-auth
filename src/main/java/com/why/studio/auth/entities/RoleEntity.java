package com.why.studio.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    private int id;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

}
