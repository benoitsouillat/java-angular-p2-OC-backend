package com.openclassrooms.etudiant.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.etudiant.dto.student.CreateDTO;
import com.openclassrooms.etudiant.dto.student.UpdateDTO;
import com.openclassrooms.etudiant.dto.student.DeleteDTO;

import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCrudService {
    private final UserRepository userRepository;

    public User create(CreateDTO createDTO) {
        if (this.verifyExistingUser(createDTO.getLogin())) {
            throw new IllegalArgumentException("User with login " + createDTO.getLogin() + " already exists");
        }
        try {
            User user = new User();
            user.setLogin(createDTO.getLogin());
            user.setFirstName(createDTO.getFirstName());
            user.setLastName(createDTO.getLastName());
            user.setPassword("PASSWORDHAVETOBECHANGED");
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    private boolean verifyExistingUser(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

}
