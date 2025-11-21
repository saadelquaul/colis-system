package com.logistique.colis_system.service;


import com.logistique.colis_system.dto.TransporteurDTO;
import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;
import com.logistique.colis_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TransporteurService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Transporteur createTransporteur(TransporteurDTO transporteurDTO) {
        if (userRepository.findByLogin(transporteurDTO.getLogin()).isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        Transporteur transporteur = new Transporteur();
        transporteur.setLogin(transporteurDTO.getLogin());
        transporteur.setPassword(passwordEncoder.encode(transporteurDTO.getPassword()));
        transporteur.setSpecialite(transporteurDTO.getSpecialite());
        transporteur.setStatut(TransporteurStatut.DISPONIBLE);
        transporteur.setActive(true);

        return userRepository.save(transporteur);
    }

    public Page<Transporteur> getAllTransporteurs(Specialite specialite, Pageable pageable) {
        if (specialite != null) {
            return userRepository.findByRoleAndSpecialite(
                    com.logistique.colis_system.model.enums.Role.TRANSPORTEUR,
                    specialite,
                    pageable
            );
        } else {
            return userRepository.findByRole(
                    Role.TRANSPORTEUR,
                    pageable
            );
        }
    }
}
