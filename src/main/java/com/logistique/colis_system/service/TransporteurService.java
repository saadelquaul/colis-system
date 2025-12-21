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

    public void deleteTransporteur(String id) {
        userRepository.deleteById(id);
    }

    public Transporteur updateTransporteur(String id, TransporteurDTO dto) {
        if(!userRepository.existsById(dto.getId())) {
            throw new RuntimeException("Transporteur not found");
        }

        Transporteur transporteur = (Transporteur) userRepository.findById(id).get();
        transporteur.setLogin(dto.getLogin());
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            transporteur.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        transporteur.setSpecialite(dto.getSpecialite());
        transporteur.setStatut(dto.getStatut());
        transporteur.setActive(dto.isActive());
        return userRepository.save(transporteur);
    }
}
