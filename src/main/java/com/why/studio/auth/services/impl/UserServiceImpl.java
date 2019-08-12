package com.why.studio.auth.services.impl;

import com.why.studio.auth.dto.UserDto;
import com.why.studio.auth.entities.RoleEntity;
import com.why.studio.auth.entities.UserEntity;
import com.why.studio.auth.repositories.RoleRepository;
import com.why.studio.auth.repositories.UserRepository;
import com.why.studio.auth.services.UserService;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.why.studio.auth.util.Utils.convertCollectionToList;
import static com.why.studio.auth.util.Utils.getUuid;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService yConversionService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private static final String PWD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";

    @Override
    public void initialCreateUser(String email) {
        String role = "ROLE_USER";
        initialCreate(email, role);
    }

    @Override
    public void initialCreateSpecialist(String email) {
        String role = "ROLE_SPECIALIST";
        initialCreate(email, role);
    }

    @Override
    public void initialCreateAdmin(String email) {
        String role = "ROLE_ADMIN";
        initialCreate(email, role);
    }

    @Override
    public void createUser(UserDto userDto) {
        userDto.setEmail(userDto.getEmail().toLowerCase());
        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь не найден по email=" +
                        userDto.getEmail()));
        if (userEntity.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Такой пользователь уже существует и активен");
        }
        String password = userDto.getPassword();
        checkPasswordMatchesPattern(userDto.getPassword());
        userEntity = yConversionService.convert(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        activateUser(userDto.getEmail());
    }

    @Override
    public void save(UserDto userDto) {
        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь не найден по email=" +
                        userDto.getEmail()));
        if (!userEntity.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь должен быть активен: " +
                    userDto.getEmail());
        }
        userRepository.save(yConversionService.convert(userDto, UserEntity.class));
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
    public UUID getActiveUserIdByEmail(String email) {
        return userRepository.findByEmailAndEnabledIsTrue(email)
                .map(UserEntity::getId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Активный пользователь не найден по email=" + email));
    }

    @Override
    public UserDto getUserById(String id) {
        return yConversionService.convert(
                userRepository.findById(getUuid(id)).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Пользователь не найден по id=" + id)),
                UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers(Optional<Boolean> active) {
        Set<UserEntity> usersSet;
        if (active.isPresent()) {
            usersSet = active.get() ? userRepository.findAllByEnabledIsTrue() : userRepository.findAllByEnabledIsFalse();
        } else {
            usersSet = userRepository.findAll();
        }
        List<UserDto> userDtos = convertCollectionToList(usersSet, yConversionService, UserEntity.class, UserDto.class);
        userDtos.sort(Comparator.comparing(UserDto::getLastName));
        return userDtos;
    }

    @Override
    public void changePassword(String email, String oldPass, String newPass) {
        checkPasswordMatchesPattern(newPass);
        userRepository.findByEmailAndEnabledIsTrue(email)
                .ifPresentOrElse(ue -> {
                    if (!passwordEncoder.matches(oldPass, ue.getPassword())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Текущий пароль не совпадает с введенным");
                    }
                    ue.setPassword(passwordEncoder.encode(newPass));
                    userRepository.save(ue);
                }, () -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Пользователь не найден по email=" + email);
                });
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

    private void initialCreate(String email, String role) {
        if (!userRepository.findByEmail(email.toLowerCase()).isEmpty()) {
            String msg = "Пользователь с таким email уже существует";
            log.error(msg);
            throw new ResponseStatusException(HttpStatus.CONFLICT, msg);
        }
        userRepository.save(UserEntity.builder()
                .email(email)
                .roles(Set.of(roleRepository.findByName(role).get()))
                .build());
    }

    private void checkPasswordMatchesPattern(String password) {
        if (!password.matches(PWD_PATTERN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пароль должен быть не менее 6 символов длиной" +
                    " и должен содержать числа и символы в верхнем и нижнем регистрах");
        }
    }
}
