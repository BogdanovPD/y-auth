package org.why.studio.auth.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.why.studio.auth.dto.UserDto;
import org.why.studio.auth.entities.RoleEntity;
import org.why.studio.auth.entities.UserEntity;
import org.why.studio.auth.repositories.UserRepository;
import org.why.studio.auth.services.UserService;

import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService yConversionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void initialCreate(String email) {
        userRepository.findByEmail(email.toLowerCase()).ifPresentOrElse(
                u -> {
                    String msg = "Пользователь с таким email уже существует";
                    log.error(msg);
                    throw new ResponseStatusException(HttpStatus.CONFLICT, msg);
                },
                () -> userRepository.save(UserEntity.builder()
                        .email(email)
                        .build()));
    }

    @Override
    public void createUser(UserDto userDto) {
        userDto.setEmail(userDto.getEmail().toLowerCase());
        UserEntity userEntity = yConversionService.convert(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userEntity);
        activateUser(userDto.getEmail());
    }

    @Override
    public void save(UserDto userDto) {
        userRepository.findByEmailAndEnabledIsFalse(userDto.getEmail()).ifPresentOrElse(
                u -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь должен быть активен: " +
                            userDto.getEmail());
                },
                () -> userRepository.save(yConversionService.convert(userDto, UserEntity.class)));
    }

    @Override
    public void activateUser(String email) {
        userRepository.findByEmailAndEnabledIsFalse(email.toLowerCase()).ifPresentOrElse(
                u -> {
                    u.setEnabled(true);
                    userRepository.save(u);
                },
                () -> log.warn("Неактивный пользователь не найден: " + email));
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndEnabledIsTrue(email.toLowerCase())
                .map(
                        u -> new User(u.getEmail(), u.getPassword(), u.getRoles().stream()
                                .map(RoleEntity::getName)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet()))
                )
                .orElseThrow(
                        () -> {
                            String msg = "Пользователь не найден: " + email;
                            log.error(msg);
                            return new UsernameNotFoundException(msg);
                        }
                );
    }
}
