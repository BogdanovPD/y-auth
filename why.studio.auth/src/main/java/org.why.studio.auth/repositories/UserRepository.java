package org.why.studio.auth.repositories;

import org.springframework.data.repository.CrudRepository;
import org.why.studio.auth.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndEnabledIsTrue(String email);
    Optional<UserEntity> findByEmailAndEnabledIsFalse(String email);

}
