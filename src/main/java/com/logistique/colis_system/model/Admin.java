package com.logistique.colis_system.model;


import com.logistique.colis_system.model.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(callSuper = true)
public class Admin extends User{
    public Admin() {
        super(Role.ADMIN);
    }
}
