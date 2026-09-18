package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.dto.LoginResponseDTO;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(User user) {
        Assert.notNull(user, "User must not be null");
        log.info("Registering new user");

        Optional<User> optionalUser = userRepository.findByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User with login " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public LoginResponseDTO login(String login, String password) {
        Assert.notNull(login, "Login must not be null");
        Assert.notNull(password, "Password must not be null");
        Optional<User> user = userRepository.findByLogin(login);
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setLogin(login);
        if (!user.isPresent()) {
            responseDTO.setToken(null);
            responseDTO.setCode(404);
            responseDTO.setMessage("User not found");
            
            return responseDTO; 
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getLogin())
                .password(user.get().getPassword())
                .build();
            
        responseDTO.setToken(jwtService.generateToken(userDetails));
        responseDTO.setCode(200);
        responseDTO.setMessage("Login successful");

        return responseDTO;
    }


}
