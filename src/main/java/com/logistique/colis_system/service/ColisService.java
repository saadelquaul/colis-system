package com.logistique.colis_system.service;

import com.logistique.colis_system.dto.request.ColisRequestDTO;
import com.logistique.colis_system.dto.response.ColisResponseDTO;
import com.logistique.colis_system.exception.BusinessException;
import com.logistique.colis_system.exception.ResourceNotFoundException;
import com.logistique.colis_system.mapper.ColisMapper;
import com.logistique.colis_system.model.*;
import com.logistique.colis_system.model.enums.*;
import com.logistique.colis_system.repository.ColisRepository;
import com.logistique.colis_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColisService {

    @Autowired
    private ColisRepository colisRepository;

    @Autowired
    private ColisMapper colisMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ColisResponseDTO createColis(ColisRequestDTO dto) {
        Colis colis;
        switch (dto.getType()) {
            case FRAGILE -> {
                ColisFragile f = new ColisFragile();
                f.setInstructionsManutention(dto.getInstructionsManutention());
                colis = f;
            }
            case FRIGO -> {
                ColisFrigo f = new ColisFrigo();
                f.setTemperatureMin(dto.getTemperatureMin());
                f.setTemperatureMax(dto.getTemperatureMax());
                colis = f;
            }
            default -> colis = new ColisStandard();
        }

        colis.setPoids(dto.getPoids());
        colis.setAddresseDestination(dto.getAddresseDestination());
        colis.setTypeColis(dto.getType());
        colis.setStatut(ColisStatut.EN_ATTENTE);

        return colisMapper.toResponse(colisRepository.save(colis));
    }


    @Transactional
    public ColisResponseDTO assignerColis(String colisId, String transporteurId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable"));

        Transporteur transporteur = (Transporteur) userRepository.findById(transporteurId)
                .filter(u -> u instanceof Transporteur)
                .orElseThrow(() -> new ResourceNotFoundException("Transporteur introuvable"));


        if (colis.getTypeColis() != transporteur.getSpecialite()) {
            throw new BusinessException("Inadéquation : Le colis est " + colis.getTypeColis() +
                    " mais le transporteur est spécialisé en " + transporteur.getSpecialite());
        }


        if (transporteur.getStatut() != TransporteurStatut.DISPONIBLE) {
            throw new BusinessException("Le transporteur n'est pas disponible actuellement.");
        }

        colis.setAssigneA(transporteur);
        colis.setStatut(ColisStatut.EN_TRANSIT);


        transporteur.setStatut(TransporteurStatut.EN_LIVRAISON);
        userRepository.save(transporteur);

       return colisMapper.toResponse(colisRepository.save(colis));
    }


    @Transactional
    public ColisResponseDTO updateStatut(String id, ColisStatut nouveauStatut) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colis introuvable"));

        colis.setStatut(nouveauStatut);


        if ((nouveauStatut == ColisStatut.LIVRE || nouveauStatut == ColisStatut.ANNULE) && colis.getAssigneA() != null) {
            Transporteur t = colis.getAssigneA();
            t.setStatut(TransporteurStatut.DISPONIBLE);
            userRepository.save(t);
        }

        return colisMapper.toResponse(colisRepository.save(colis));
    }


    public Page<ColisResponseDTO> getColisList(Specialite type, ColisStatut statut, Pageable pageable) {
        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN) {
            if (type != null && statut != null) return colisRepository.findByTypeColisAndStatut(type, statut, pageable).map(colisMapper::toResponse);
            if (type != null) return colisRepository.findByTypeColis(type, pageable).map(colisMapper::toResponse);
            if (statut != null) return colisRepository.findByStatut(statut, pageable).map(colisMapper::toResponse);
            return colisRepository.findAll(pageable).map(colisMapper::toResponse);
        } else {
            return colisRepository.findByAssigneA_Id(currentUser.getId(), pageable).map(colisMapper::toResponse);
        }
    }


    public Page<ColisResponseDTO> searchByDestination(String destination, Pageable pageable) {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN) {
            return colisRepository.findByAddresseDestinationContainingIgnoreCase(destination, pageable).map(colisMapper::toResponse);
        } else {
            return colisRepository.findByAssigneA_IdAndAddresseDestinationContainingIgnoreCase(
                    currentUser.getId(), destination, pageable).map(colisMapper::toResponse);
        }
    }

    @Transactional
    public void deleteColis(String id) {
        if (!colisRepository.existsById(id)) throw new ResourceNotFoundException("Colis introuvable");
        colisRepository.deleteById(id);
    }

    // --- Private Helpers ---

    private User getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur actuel introuvable"));
    }
}