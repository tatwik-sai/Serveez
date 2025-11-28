package com.example.serveez.controller;

import com.example.serveez.dto.request.LoginRequest;
import com.example.serveez.dto.request.SignupRequest;
import com.example.serveez.dto.response.ApiResponse;
import com.example.serveez.dto.response.AuthResponse;
import com.example.serveez.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.signup(request, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.adminLogin(request, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(new ApiResponse(true, "Logged out successfully"));
    }
}
