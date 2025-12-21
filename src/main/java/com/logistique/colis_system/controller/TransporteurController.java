package com.logistique.colis_system.controller;

import com.logistique.colis_system.dto.request.TransporteurRequestDTO;
import com.logistique.colis_system.dto.response.TransporteurResponseDTO;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.service.TransporteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class TransporteurController {

    @Autowired
    private TransporteurService transporteurService;

    @GetMapping("admin/transporteur")
    public ResponseEntity<Page<TransporteurResponseDTO>> getAllTransporteurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Specialite specialite) {

        return ResponseEntity.ok(transporteurService.getAllTransporteurs(specialite, PageRequest.of(page, size)));
    }

    @PostMapping("admin/transporteur")
    public ResponseEntity<TransporteurResponseDTO> createTransporteur(@RequestBody TransporteurRequestDTO dto) {
        return ResponseEntity.ok(transporteurService.createTransporteur(dto));
    }

    @DeleteMapping("admin/transporteur/{id}")
    public ResponseEntity<String> deleteTransporteur(@PathVariable String id) {
        transporteurService.deleteTransporteur(id);
        return ResponseEntity.ok("The Transporteur with the id: " + id + " Deleted Successfully.");
    }

    @PutMapping("admin/transporteur/{id}")
    public ResponseEntity<TransporteurResponseDTO> updateTransporteur(@PathVariable String id, @RequestBody TransporteurRequestDTO dto) {
        return ResponseEntity.ok(transporteurService.updateTransporteur(id, dto));
    }

}
