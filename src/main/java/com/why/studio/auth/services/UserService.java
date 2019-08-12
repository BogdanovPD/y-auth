package com.why.studio.auth.services;

import com.why.studio.auth.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    void initialCreateUser(String email);

    void initialCreateSpecialist(String email);

    void initialCreateAdmin(String email);

    void createUser(UserDto userDto);

    void save(UserDto userDto);

    void activateUser(String email);

    UUID getActiveUserIdByEmail(String email);

    UserDto getUserById(String id);

    List<UserDto> getAllUsers(Optional<Boolean> active);
    void changePassword(String email, String oldPass, String newPass);

}
