package com.example.etudiants.repository;

import com.example.etudiants.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    List<Etudiant> findByAnneePremiereInscription(int annee);

    List<Etudiant> findByDepartementId(Long departementId);
}
