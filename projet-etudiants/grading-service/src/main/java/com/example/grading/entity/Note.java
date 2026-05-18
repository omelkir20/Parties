package com.example.grading.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    @NotBlank
    private String matiere;

    @Column(nullable = false)
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double valeur;
}
