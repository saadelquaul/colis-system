package com.logistique.colis_system.mapper;

import com.logistique.colis_system.dto.response.TransporteurResponseDTO;
import com.logistique.colis_system.dto.response.UserResponseDTO;
import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.User;
import org.springframework.stereotype.Component;

@Component
public class TransporteurMapper {

    public TransporteurResponseDTO toResponse(Transporteur transporteur) {
        return  TransporteurResponseDTO.builder()
                .id(transporteur.getId())
                .login(transporteur.getLogin())
                .role(transporteur.getRole())
                .specialite(transporteur.getSpecialite())
                .statut(transporteur.getStatut())
                .isActive(transporteur.isActive()).build();
    }
}
