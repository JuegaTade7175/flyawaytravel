package com.example.flyawaytravel.controller;

import com.example.flyawaytravel.dto.request.UserRegisterRequest;
import com.example.flyawaytravel.dto.response.UserIdResponse;
import com.example.flyawaytravel.dto.response.UserResponse;
import com.example.flyawaytravel.entity.User;
import com.example.flyawaytravel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<UserIdResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserIdResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(response);
    }
}