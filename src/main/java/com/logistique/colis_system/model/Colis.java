package com.logistique.colis_system.model;


import com.logistique.colis_system.model.enums.ColisStatut;
import com.logistique.colis_system.model.enums.Specialite;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "colis")
public abstract class Colis {

    @Id
    private String id;

    private double poids;
    private String addresseDestination;
    private ColisStatut statut;

    private Specialite typeColis;

    @DBRef
    private Transporteur assigneA;

    public Colis(Specialite typeColis) {
        this.typeColis = typeColis;
        this.statut = ColisStatut.EN_ATTENTE;
    }

}
