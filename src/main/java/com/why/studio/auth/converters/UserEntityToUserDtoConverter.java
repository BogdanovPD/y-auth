package com.why.studio.auth.converters;

import com.why.studio.auth.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.why.studio.auth.entities.UserEntity;

@Component
public class UserEntityToUserDtoConverter implements Converter<UserEntity, UserDto> {

    @Override
    public UserDto convert(UserEntity userEntity) {
        return UserDto.builder()
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .role("")
                .password("")
                .isActive(userEntity.isEnabled())
                .build();
    }
}
