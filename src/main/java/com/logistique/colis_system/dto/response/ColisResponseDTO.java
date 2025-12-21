package com.logistique.colis_system.dto.response;

import com.logistique.colis_system.model.enums.ColisStatut;
import com.logistique.colis_system.model.enums.Specialite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColisResponseDTO {

    private String id;
    private Double poids;
    private String adresseDestination;
    private Specialite typeColis;
    private ColisStatut statut;

    // Assignment Info
    private String transporteurId;
    private String transporteurLogin;

    // Fields specific to ColisFragile
    private String instructionsManutention;

    // Fields specific to ColisFrigo
    private Double temperatureMin;
    private Double temperatureMax;
}