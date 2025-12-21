package com.logistique.colis_system.dto.response;

import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransporteurResponseDTO {
    private String id;

    private String login;

    private Specialite specialite;

    private TransporteurStatut statut;

    private Role role;

    private boolean isActive;
}
