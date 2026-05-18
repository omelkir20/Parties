package com.example.etudiants.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartementDTO {
    private Long id;

    @NotBlank(message = "Le nom du département est obligatoire")
    private String nom;
}
