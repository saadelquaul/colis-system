package com.logistique.colis_system.repository;


import com.logistique.colis_system.model.Colis;
import com.logistique.colis_system.model.enums.ColisStatut;
import com.logistique.colis_system.model.enums.Specialite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends MongoRepository<Colis, String> {

    List<Colis> findByAssigneA_Id(String transporteurId);

    List<Colis> findByAdresseDestinationContaining(String destination);

    List<Colis> findByStatut(ColisStatut statut);

    List<Colis> findByTypeColis(Specialite typeColis);

    List<Colis> findByAssigneA_IdAndStatut(String transporteurId, ColisStatut statut);
}
