package com.logistique.colis_system.config;

import com.logistique.colis_system.model.Admin;
import com.logistique.colis_system.model.enums.Role;
import com.logistique.colis_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByRole(Role.ADMIN).isEmpty()) {
            Admin admin = new Admin();
            admin.setLogin("admin");

            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setActive(true);

            userRepository.save(admin);
        }
    }
}
