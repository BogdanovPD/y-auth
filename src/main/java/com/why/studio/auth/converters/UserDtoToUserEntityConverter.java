package com.why.studio.auth.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.why.studio.auth.dto.UserDto;
import com.why.studio.auth.entities.RoleEntity;
import com.why.studio.auth.entities.UserEntity;
import com.why.studio.auth.repositories.RoleRepository;
import com.why.studio.auth.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserDtoToUserEntityConverter implements Converter<UserDto, UserEntity> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public UserEntity convert(UserDto userDto) {
        Optional<UserEntity> userEntityOpt = userRepository.findByEmail(userDto.getEmail());
        if (userEntityOpt.isPresent()) {
            return updateData(userEntityOpt.get(), userDto);
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле email не заполнено!");
        }
        return UserEntity.builder()
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .roles(Set.of(getRole(userDto.getRole())))
                .build();
    }

    private UserEntity updateData(UserEntity userEntity, UserDto userDto) {
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPhone(userDto.getPhone());
        return userEntity;
    }

    private RoleEntity getRole(String role) {
        return roleRepository.findByName(role).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь не сохранен! " +
                        "Роль не найдена: " + role));
    }
}
