package org.why.studio.auth.repositories;

import org.springframework.data.repository.CrudRepository;
import org.why.studio.auth.entities.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(String name);

}
