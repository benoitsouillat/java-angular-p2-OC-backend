package com.openclassrooms.etudiant.controller;

import com.openclassrooms.etudiant.dto.LoginRequestDTO;
import com.openclassrooms.etudiant.dto.LoginResponseDTO;
import com.openclassrooms.etudiant.dto.RegisterDTO;
import com.openclassrooms.etudiant.dto.student.CreateDTO;
import com.openclassrooms.etudiant.dto.student.UpdateDTO;
import com.openclassrooms.etudiant.dto.student.DeleteDTO;
import com.openclassrooms.etudiant.entities.User;
import com.openclassrooms.etudiant.mapper.UserDtoMapper;
import com.openclassrooms.etudiant.service.UserService;
import com.openclassrooms.etudiant.service.StudentCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StudentCrudService studentCrudService;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(userDtoMapper.toEntity(registerDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO responseDTO = userService.login(loginRequestDTO.getLogin(), loginRequestDTO.getPassword());
        switch (responseDTO.getCode().intValue()) {
            case 200:
                return ResponseEntity.ok(responseDTO);
            case 400:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
            case 401:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDTO);
            case 403:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
            case 404:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    @PostMapping("/api/student")
    public ResponseEntity<?> createStudent(@RequestHeader("Authorization") String authorization , @Valid @RequestBody CreateDTO createDTO ) {
         if(!userService.isAuthenticated(authorization)) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
         }
         if(!userService.isAuthenticated(authorization)) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
         }

         User user = studentCrudService.create(createDTO);
         if (user != null)
         {
             return ResponseEntity.status(HttpStatus.CREATED).body("User " + user.getLogin() + " was created successfully");
         }

         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
    }

}
