package com.why.studio.auth.repositories;

import org.springframework.data.repository.CrudRepository;
import com.why.studio.auth.entities.UserEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    Set<UserEntity> findAll();
    Set<UserEntity> findAllByEnabledIsTrue();
    Set<UserEntity> findAllByEnabledIsFalse();
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndEnabledIsTrue(String email);
    Optional<UserEntity> findByEmailAndEnabledIsFalse(String email);

}
