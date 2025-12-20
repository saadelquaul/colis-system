package com.logistique.colis_system.mapper;


import com.logistique.colis_system.dto.response.UserResponseDTO;
import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole())
                .isActive(user.isActive()).build();
        if(user instanceof Transporteur) {
            Transporteur transporteur = (Transporteur) user;
            userResponseDTO.setSpecialite(transporteur.getSpecialite());
            userResponseDTO.setTransporteurStatut(transporteur.getStatut());
        }

        return userResponseDTO;
    }
}
