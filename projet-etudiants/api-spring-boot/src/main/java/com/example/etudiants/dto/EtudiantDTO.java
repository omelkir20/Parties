package com.example.etudiants.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
    private Long id;

    @NotBlank(message = "Le CIN est obligatoire")
    private String cin;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private int anneePremiereInscription;

    private Long departementId;
    private String departementNom;

    private int age;
}
