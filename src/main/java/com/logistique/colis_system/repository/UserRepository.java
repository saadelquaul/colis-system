package com.logistique.colis_system.repository;

import com.logistique.colis_system.model.Transporteur;
import com.logistique.colis_system.model.User;
import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);
    List<User> findByRole(Role role);


    Page<User> findByActiveTrue(Pageable pageable);
    
    @Query("{ 'role': 'TRANSPORTEUR', 'specialite': ?0 }")
    List<Transporteur> findTransporteursBySpecialite(Specialite specialite);

    Page<Transporteur> findByRole(Role role, Pageable pageable);
    Page<Transporteur> findByRoleAndSpecialite(Role role, Specialite specialite, Pageable pageable);

}
