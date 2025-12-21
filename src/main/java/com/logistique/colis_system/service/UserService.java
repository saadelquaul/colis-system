package com.logistique.colis_system.service;


import com.logistique.colis_system.dto.response.UserResponseDTO;
import com.logistique.colis_system.exception.BusinessException;
import com.logistique.colis_system.exception.ResourceNotFoundException;
import com.logistique.colis_system.mapper.UserMapper;
import com.logistique.colis_system.model.User;
import com.logistique.colis_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Transactional
    public void setUserActiveStatus(String userId, boolean status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable avec l'ID: " + userId));


        String currentAdmin = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getLogin().equals(currentAdmin) && !status) {
            throw new BusinessException("Vous ne pouvez pas désactiver votre propre compte administrateur.");
        }

        user.setActive(status);
        userRepository.save(user);
    }
}
