package org.why.studio.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.why.studio.auth.dto.UserDto;
import org.why.studio.auth.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "user/create/init", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> initialCreateUser(@RequestBody String email) {
        userService.initialCreate(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "user/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }

}
