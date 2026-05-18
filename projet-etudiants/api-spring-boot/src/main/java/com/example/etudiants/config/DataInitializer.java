package com.example.etudiants.config;

import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EtudiantRepository etudiantRepository;
    private final DepartementRepository departementRepository;

    @Override
    public void run(String... args) {
        if (departementRepository.count() == 0) {
            Departement info = departementRepository.save(
                    Departement.builder().nom("Informatique").build());
            Departement math = departementRepository.save(
                    Departement.builder().nom("Mathématiques").build());
            Departement phys = departementRepository.save(
                    Departement.builder().nom("Physique").build());

            etudiantRepository.saveAll(List.of(
                    Etudiant.builder().cin("AB123456").nom("Alice Martin")
                            .dateNaissance(LocalDate.of(2001, 3, 15))
                            .email("alice.martin@example.com")
                            .anneePremiereInscription(2020).departement(info).build(),
                    Etudiant.builder().cin("CD789012").nom("Bob Dupont")
                            .dateNaissance(LocalDate.of(2000, 7, 22))
                            .email("bob.dupont@example.com")
                            .anneePremiereInscription(2019).departement(math).build(),
                    Etudiant.builder().cin("EF345678").nom("Clara Lefevre")
                            .dateNaissance(LocalDate.of(2002, 11, 8))
                            .email("clara.lefevre@example.com")
                            .anneePremiereInscription(2021).departement(info).build(),
                    Etudiant.builder().cin("GH901234").nom("David Bernard")
                            .dateNaissance(LocalDate.of(1999, 5, 30))
                            .email("david.bernard@example.com")
                            .anneePremiereInscription(2018).departement(phys).build(),
                    Etudiant.builder().cin("IJ567890").nom("Emma Rousseau")
                            .dateNaissance(LocalDate.of(2003, 1, 14))
                            .email("emma.rousseau@example.com")
                            .anneePremiereInscription(2022).departement(math).build()
            ));
        }
    }
}
