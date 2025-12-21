package com.logistique.colis_system.model;


import com.logistique.colis_system.model.enums.Specialite;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ColisFrigo extends Colis{

    private Double temperatureMin;
    private Double temperatureMax;

    public ColisFrigo() {
        super(Specialite.FRIGO);
    }
}
