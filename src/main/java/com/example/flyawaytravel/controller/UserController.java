package com.example.flyawaytravel.controller;

import com.example.flyawaytravel.dto.request.UserRegisterRequest;
import com.example.flyawaytravel.dto.response.NewIdResponse;
import com.example.flyawaytravel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<NewIdResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        NewIdResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(new NewIdResponse(user.getId()));
    }
}