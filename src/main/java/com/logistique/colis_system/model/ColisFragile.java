package com.logistique.colis_system.model;


import com.logistique.colis_system.model.enums.Specialite;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ColisFragile extends Colis{

    private String instructionsManutention;

    public ColisFragile() {
        super(Specialite.FRAGILE);
    }
}
