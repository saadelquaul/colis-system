package com.logistique.colis_system.service;

import com.logistique.colis_system.dto.response.ColisResponseDTO;
import com.logistique.colis_system.exception.BusinessException;
import com.logistique.colis_system.mapper.ColisMapper;
import com.logistique.colis_system.model.*;
import com.logistique.colis_system.model.enums.*;
import com.logistique.colis_system.repository.ColisRepository;
import com.logistique.colis_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColisServiceTest {

    @Mock
    private ColisRepository colisRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ColisMapper colisMapper;

    @InjectMocks
    private ColisService colisService;

    private ColisFrigo sampleColis;
    private Transporteur compatibleTransporteur;
    private Transporteur incompatibleTransporteur;
    private ColisResponseDTO sampleResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize a FRIGO parcel
        sampleColis = new ColisFrigo();
        sampleColis.setId("colis-123");
        sampleColis.setTypeColis(Specialite.FRIGO);
        sampleColis.setStatut(ColisStatut.EN_ATTENTE);

        // Initialize a compatible FRIGO transporter
        compatibleTransporteur = new Transporteur();
        compatibleTransporteur.setId("trans-ok");
        compatibleTransporteur.setSpecialite(Specialite.FRIGO);
        compatibleTransporteur.setStatut(TransporteurStatut.DISPONIBLE);

        // Initialize an incompatible STANDARD transporter
        incompatibleTransporteur = new Transporteur();
        incompatibleTransporteur.setId("trans-bad");
        incompatibleTransporteur.setSpecialite(Specialite.STANDARD);
        incompatibleTransporteur.setStatut(TransporteurStatut.DISPONIBLE);

        sampleResponseDTO = new ColisResponseDTO();
        sampleResponseDTO.setId("colis-123");
        sampleResponseDTO.setStatut(ColisStatut.EN_TRANSIT);
        sampleResponseDTO.setTransporteurId("trans-ok");
    }

    @Test
    @DisplayName("Assignment should succeed when specialties match and transporter is available")
    void assignColis_Success() {

        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(sampleColis));
        when(userRepository.findById("trans-ok")).thenReturn(Optional.of(compatibleTransporteur));
        when(colisRepository.save(any(Colis.class))).thenReturn(sampleColis);

        when(colisMapper.toResponse(any(Colis.class))).thenReturn(sampleResponseDTO);

        ColisResponseDTO result = colisService.assignerColis("colis-123", "trans-ok");


        assertNotNull(result);
        assertEquals(ColisStatut.EN_TRANSIT, result.getStatut());
        assertEquals(TransporteurStatut.EN_LIVRAISON, compatibleTransporteur.getStatut());
        assertEquals("trans-ok", result.getTransporteurId());
        verify(colisRepository).save(any(Colis.class));
    }

    @Test
    @DisplayName("Assignment should fail when specialties do not match")
    void assignColis_SpecialtyMismatch() {

        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(sampleColis));
        when(userRepository.findById("trans-bad")).thenReturn(Optional.of(incompatibleTransporteur));


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            colisService.assignerColis("colis-123", "trans-bad");
        });

        assertTrue(exception.getMessage().contains("Inadéquation"));
        verify(colisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Assignment should fail when transporter is already busy")
    void assignColis_TransporterBusy() {

        compatibleTransporteur.setStatut(TransporteurStatut.EN_LIVRAISON); // Busy
        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(sampleColis));
        when(userRepository.findById("trans-ok")).thenReturn(Optional.of(compatibleTransporteur));


        assertThrows(BusinessException.class, () -> {
            colisService.assignerColis("colis-123", "trans-ok");
        });
    }
}