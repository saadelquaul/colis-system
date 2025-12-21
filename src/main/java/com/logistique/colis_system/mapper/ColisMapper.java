package com.logistique.colis_system.mapper;

import com.logistique.colis_system.dto.response.ColisResponseDTO;
import com.logistique.colis_system.model.*;
import org.springframework.stereotype.Component;

@Component
public class ColisMapper {

    public ColisResponseDTO toResponse(Colis colis) {
        ColisResponseDTO dto = new ColisResponseDTO();

        dto.setId(colis.getId());
        dto.setPoids(colis.getPoids());
        dto.setAdresseDestination(colis.getAddresseDestination());
        dto.setTypeColis(colis.getTypeColis());
        dto.setStatut(colis.getStatut());

        // Map assignment info if present
        if (colis.getAssigneA() != null) {
            dto.setTransporteurId(colis.getAssigneA().getId());
            dto.setTransporteurLogin(colis.getAssigneA().getLogin());
        }

        if (colis instanceof ColisFragile fragile) {
            dto.setInstructionsManutention(fragile.getInstructionsManutention());
        } else if (colis instanceof ColisFrigo frigo) {
            dto.setTemperatureMin(frigo.getTemperatureMin());
            dto.setTemperatureMax(frigo.getTemperatureMax());
        }

        return dto;
    }
}