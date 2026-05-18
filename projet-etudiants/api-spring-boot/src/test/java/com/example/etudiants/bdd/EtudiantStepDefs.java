package com.example.etudiants.bdd;

import com.example.etudiants.entity.Etudiant;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EtudiantStepDefs {

    private Etudiant etudiant;
    private int age;

    @Etantdonné("un étudiant avec la date de naissance {string}")
    public void unEtudiantAvecLaDateDeNaissance(String dateStr) {
        etudiant = new Etudiant();
        etudiant.setDateNaissance(LocalDate.parse(dateStr));
    }

    @Quand("on calcule son âge")
    public void onCalculeSonAge() {
        age = etudiant.age();
    }

    @Alors("l'âge retourné doit être supérieur ou égal à {int}")
    public void lAgeRetourneDvoitEtreSuperieurOuEgalA(int ageAttendu) {
        assertTrue(age >= ageAttendu,
                "L'âge attendu >= " + ageAttendu + " mais obtenu : " + age);
    }
}
