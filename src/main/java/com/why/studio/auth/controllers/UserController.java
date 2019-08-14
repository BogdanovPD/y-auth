package com.why.studio.auth.controllers;

import com.why.studio.auth.dto.ChangePassDto;
import com.why.studio.auth.dto.UserDto;
import com.why.studio.auth.dto.UserInitDto;
import com.why.studio.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "user/client/create/init")
    public ResponseEntity<?> initialCreateUser(@RequestBody UserInitDto initDto) {
        userService.initialCreateUser(initDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "user/spec/create/init")
    public ResponseEntity<?> initialCreateSpec(@RequestBody UserInitDto initDto) {
        userService.initialCreateSpecialist(initDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "user/admin/create/init")
    public ResponseEntity<?> initialCreateAdmin(@RequestBody UserInitDto initDto) {
        userService.initialCreateAdmin(initDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "user/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "user/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "user/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody ChangePassDto changePassDto) {
        userService.changePassword(changePassDto.getEmail(), changePassDto.getOldPass(), changePassDto.getNewPass());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(value = "/user/all")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam("active") Optional<Boolean> active) {
        return ResponseEntity.ok(userService.getAllUsers(active));
    }

}
