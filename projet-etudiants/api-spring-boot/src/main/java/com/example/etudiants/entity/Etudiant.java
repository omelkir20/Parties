package com.example.etudiants.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "etudiants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cin;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Column(nullable = false, unique = true)
    private String email;

    private int anneePremiereInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id")
    private Departement departement;

    public int age() {
        return Period.between(this.dateNaissance, LocalDate.now()).getYears();
    }
}
