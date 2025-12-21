package com.logistique.colis_system.repository;


import com.logistique.colis_system.model.Colis;
import com.logistique.colis_system.model.enums.ColisStatut;
import com.logistique.colis_system.model.enums.Specialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends MongoRepository<Colis, String> {

    Page<Colis> findByAssigneA_Id(String transporteurId, Pageable pageable);

    Page<Colis> findByAddresseDestinationContaining(String Destination, Pageable pageable);

    Page<Colis> findByStatut(ColisStatut statut, Pageable pageable);

    Page<Colis> findByTypeColis(Specialite typeColis, Pageable pageable);

    Page<Colis> findByAssigneA_IdAndStatut(String transporteurId, ColisStatut statut, Pageable pageable);

    Page<Colis> findByAddresseDestinationContainingIgnoreCase(String destination, Pageable pageable);

    Page<Colis> findByAssigneA_IdAndAddresseDestinationContainingIgnoreCase(String id,String destination, Pageable pageable);

    Page<Colis> findByTypeColisAndStatut(Specialite type, ColisStatut statut, Pageable pageable);
}
