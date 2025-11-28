package com.example.serveez.controller;

import com.example.serveez.dto.response.ApiResponse;
import com.example.serveez.model.User;
import com.example.serveez.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String role) {
        if (role != null && !role.isEmpty()) {
            return ResponseEntity.ok(userRepository.findAll().stream()
                    .filter(u -> u.getRole().name().equals(role))
                    .toList());
        }
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse> updateUserStatus(
            @PathVariable String id,
            @RequestParam boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(isActive);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "User status updated"));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> stats = new HashMap<>();
        List<User> allUsers = userRepository.findAll();

        stats.put("totalUsers", allUsers.stream().filter(u -> u.getRole() == User.UserRole.USER).count());
        stats.put("totalProviders", allUsers.stream().filter(u -> u.getRole() == User.UserRole.PROVIDER).count());
        stats.put("totalAdmins", allUsers.stream().filter(u -> u.getRole() == User.UserRole.ADMIN).count());

        return ResponseEntity.ok(stats);
    }
}
