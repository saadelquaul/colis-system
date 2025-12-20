package com.logistique.colis_system.service;


import com.logistique.colis_system.dto.response.UserResponseDTO;
import com.logistique.colis_system.mapper.UserMapper;
import com.logistique.colis_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }
}
