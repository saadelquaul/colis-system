package com.logistique.colis_system.dto.request;

import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.ColisStatut;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColisRequestDTO {
    private Double poids;
    private String adresseDestination;
    private Specialite type; // STANDARD, FRAGILE, FRIGO
    private ColisStatut statut;

    // Type-specific fields
    private String instructionsManutention; // For FRAGILE
    private Double temperatureMin;          // For FRIGO
    private Double temperatureMax;          // For FRIGO
}