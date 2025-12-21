package com.logistique.colis_system.service;

import com.logistique.colis_system.dto.request.TransporteurRequestDTO;
import com.logistique.colis_system.dto.response.TransporteurResponseDTO;
import com.logistique.colis_system.exception.BusinessException;
import com.logistique.colis_system.exception.ResourceNotFoundException;
import com.logistique.colis_system.mapper.TransporteurMapper;
import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.User;
import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;
import com.logistique.colis_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransporteurService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransporteurMapper transporteurMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public TransporteurResponseDTO createTransporteur(TransporteurRequestDTO dto) {

        if (userRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new BusinessException("Le login '" + dto.getLogin() + "' est déjà utilisé.");
        }

        Transporteur transporteur = new Transporteur();
        transporteur.setLogin(dto.getLogin());
        transporteur.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getSpecialite() == null) {
            throw new BusinessException("La spécialité est obligatoire (STANDARD, FRAGILE, ou FRIGO).");
        }

        transporteur.setSpecialite(dto.getSpecialite());
        transporteur.setStatut(TransporteurStatut.DISPONIBLE);
        transporteur.setActive(true);

        Transporteur savedTransportuer = userRepository.save(transporteur);
        return transporteurMapper.toResponse(savedTransportuer);
    }

    public Page<TransporteurResponseDTO> getAllTransporteurs(Specialite specialite, Pageable pageable) {
        if (specialite != null) {
            return userRepository.findByRoleAndSpecialite(Role.TRANSPORTEUR, specialite, pageable).map(transporteurMapper::toResponse);
        }
        return userRepository.findByRole(Role.TRANSPORTEUR, pageable).map(transporteurMapper::toResponse);
    }

    @Transactional
    public void deleteTransporteur(String id) {
        Transporteur transporteur = findTransporteurById(id);
        userRepository.delete(transporteur);
    }

    @Transactional
    public TransporteurResponseDTO updateTransporteur(String id, TransporteurRequestDTO dto) {
        Transporteur transporteur = findTransporteurById(id);

        if(dto.getLogin() != null) {
            if (!transporteur.getLogin().equals(dto.getLogin()) &&
                    userRepository.findByLogin(dto.getLogin()).isPresent()) {
                throw new BusinessException("Ce nouveau login est déjà pris.");
            }
            transporteur.setLogin(dto.getLogin());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            transporteur.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if(dto.getSpecialite() != null) transporteur.setSpecialite(dto.getSpecialite());
        if(dto.getStatut() != null)transporteur.setStatut(dto.getStatut());
        if(dto.isActive() != transporteur.isActive())transporteur.setActive(dto.isActive());

        Transporteur savedTransporteur =  userRepository.save(transporteur);
        return transporteurMapper.toResponse(savedTransporteur);
    }


    private Transporteur findTransporteurById(String id) {
        return userRepository.findById(id)
                .filter(user -> user instanceof Transporteur)
                .map(user -> (Transporteur) user)
                .orElseThrow(() -> new ResourceNotFoundException("Transporteur introuvable avec l'ID: " + id));
    }
}