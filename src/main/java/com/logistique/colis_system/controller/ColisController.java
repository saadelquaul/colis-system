package com.logistique.colis_system.controller;

import com.logistique.colis_system.dto.request.ColisRequestDTO;
import com.logistique.colis_system.dto.response.ColisResponseDTO;
import com.logistique.colis_system.model.enums.ColisStatut;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.service.ColisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Base for both roles
@Tag(name = "Gestion des Colis", description = "Endpoints unifiés pour Admin et Transporteur")
public class ColisController {

    @Autowired
    private ColisService colisService;

   // ***** ADMIN ENDPOINTS *****

    @PostMapping("/admin/colis")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Créer un colis")
    public ResponseEntity<ColisResponseDTO> create(@RequestBody ColisRequestDTO dto) {
        return ResponseEntity.ok(colisService.createColis(dto));
    }

    @PutMapping("/admin/colis/{colisId}/assign/{transporteurId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Assigner un colis")
    public ResponseEntity<ColisResponseDTO> assign(@PathVariable String colisId, @PathVariable String transporteurId) {
        return ResponseEntity.ok(colisService.assignerColis(colisId, transporteurId));
    }

    @GetMapping("/admin/colis")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Lister tous les colis")
    public ResponseEntity<Page<ColisResponseDTO>> adminList(
            @RequestParam(required = false) Specialite type,
            @RequestParam(required = false) ColisStatut statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(colisService.getColisList(type, statut, PageRequest.of(page, size)));
    }

    @GetMapping("/admin/colis/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Rechercher par destination")
    public ResponseEntity<Page<ColisResponseDTO>> adminSearch(
            @RequestParam String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(colisService.searchByDestination(destination, PageRequest.of(page, size)));
    }

    @PatchMapping("/admin/colis/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Modifier le statut")
    public ResponseEntity<ColisResponseDTO> adminUpdateStatus(@PathVariable String id, @RequestParam ColisStatut statut) {
        return ResponseEntity.ok(colisService.updateStatut(id, statut));
    }

    @DeleteMapping("/admin/colis/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Supprimer un colis")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        colisService.deleteColis(id);
        return ResponseEntity.noContent().build();
    }

    // ***** TRANSPORTEUR ENDPOINTS *****


    @GetMapping("/transporteur/colis")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    @Operation(summary = "[TRANSPORTEUR] Mes colis")
    public ResponseEntity<Page<ColisResponseDTO>> myColis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(colisService.getColisList(null, null, PageRequest.of(page, size)));
    }

    @GetMapping("/transporteur/colis/search")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    @Operation(summary = "[TRANSPORTEUR] Rechercher dans mes colis")
    public ResponseEntity<Page<ColisResponseDTO>> searchMyColis(
            @RequestParam String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(colisService.searchByDestination(destination, PageRequest.of(page, size)));
    }

    @PatchMapping("/transporteur/colis/{id}/statut")
    @PreAuthorize("hasRole('TRANSPORTEUR')")
    @Operation(summary = "[TRANSPORTEUR] Mettre à jour le statut")
    public ResponseEntity<ColisResponseDTO> updateMyStatus(@PathVariable String id, @RequestParam ColisStatut statut) {
        return ResponseEntity.ok(colisService.updateStatut(id, statut));
    }
}