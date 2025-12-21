package com.logistique.colis_system.controller;

import com.logistique.colis_system.dto.response.UserResponseDTO;
import com.logistique.colis_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/"))
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("admin/users")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PatchMapping("/admin/users/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activer un utilisateur")
    public ResponseEntity<String> activate(@PathVariable String id) {
        userService.setUserActiveStatus(id, true);
        return ResponseEntity.ok("Compte activé avec succès.");
    }

    @PatchMapping("/admin/users/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Désactiver un utilisateur")
    public ResponseEntity<String> deactivate(@PathVariable String id) {
        userService.setUserActiveStatus(id, false);
        return ResponseEntity.ok("Compte désactivé. L'utilisateur ne pourra plus se connecter.");
    }
}
