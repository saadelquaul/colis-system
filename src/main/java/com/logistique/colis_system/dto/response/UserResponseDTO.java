package com.logistique.colis_system.dto.response;


import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String id;
    private String login;
    private Role role;
    private boolean isActive;

    private Specialite specialite;
    private TransporteurStatut transporteurStatut;
}
