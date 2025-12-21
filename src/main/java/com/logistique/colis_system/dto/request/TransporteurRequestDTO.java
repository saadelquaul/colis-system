package com.logistique.colis_system.dto.request;


import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransporteurRequestDTO {

    private String id;

    @NotBlank(message = "Login is required")
    private String login;

    private String password;

    @NotNull(message = "Specialite is required (STANDARD, FRAGILE, FRIGO)")
    private Specialite specialite;

    private TransporteurStatut statut;
    private boolean active;
}
