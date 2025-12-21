package com.logistique.colis_system.model;

import com.logistique.colis_system.model.enums.Specialite;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ColisStandard extends Colis{
    public ColisStandard() {
        super(Specialite.STANDARD);
    }
}
