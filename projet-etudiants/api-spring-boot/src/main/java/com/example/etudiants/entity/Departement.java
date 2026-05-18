package com.example.etudiants.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;
}
