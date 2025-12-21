package com.logistique.colis_system.controller;


import com.logistique.colis_system.dto.TransporteurDTO;
import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.service.TransporteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminTransporteurController {

    @Autowired
    private TransporteurService transporteurService;


    // GET /api/admin/transporteurs?page=0&size=10&specialite=FRIGO
    @GetMapping("/transporteurs")
    public ResponseEntity<Page<Transporteur>> getAllTransporteurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Specialite specialite) {

        return ResponseEntity.ok(transporteurService.getAllTransporteurs(specialite, PageRequest.of(page, size)));
    }

    @PostMapping("/transporteurs")
    public ResponseEntity<Transporteur> createTransporteur(@RequestBody TransporteurDTO dto) {
        return ResponseEntity.ok(transporteurService.createTransporteur(dto));
    }

    @DeleteMapping("/transporteurs/{id}")
    public ResponseEntity<Void> deleteTransporteur(@PathVariable String id) {
        transporteurService.deleteTransporteur(id);
        return ResponseEntity.ok().build();
    }
}
