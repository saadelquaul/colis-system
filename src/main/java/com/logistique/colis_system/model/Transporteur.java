package com.logistique.colis_system.model;

import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.model.enums.Specialite;
import com.logistique.colis_system.model.enums.TransporteurStatut;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Transporteur extends User{

    private TransporteurStatut statut;
    private Specialite specialite;

    public Transporteur() {
        super(Role.TRANSPORTEUR);
    }
}
