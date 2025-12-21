package com.logistique.colis_system.model;


import com.logistique.colis_system.model.enums.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public abstract class User {

    @Id
    private String id;


    private String login;
    private String password;
    private Role role;
    private boolean active;


    public User(Role role) {
        this.role = role;
        this.active = true;
    }
}
