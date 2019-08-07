package org.why.studio.auth.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.why.studio.auth.dto.UserDto;

public interface UserService extends UserDetailsService {

    void initialCreate(String email);
    void createUser(UserDto userDto);
    void save(UserDto userDto);
    void activateUser(String email);
    boolean userExists(String email);
    UserDto getUserById(String userId);

}
