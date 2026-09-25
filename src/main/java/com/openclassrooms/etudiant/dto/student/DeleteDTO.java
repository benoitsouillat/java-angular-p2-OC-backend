package com.openclassrooms.etudiant.dto.student;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class DeleteDTO {
    @NotBlank 
    private Long id;


}
