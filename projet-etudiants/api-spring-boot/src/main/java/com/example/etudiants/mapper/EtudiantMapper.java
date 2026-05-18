package com.example.etudiants.mapper;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import org.springframework.stereotype.Component;

@Component
public class EtudiantMapper {

    public EtudiantDTO toDTO(Etudiant etudiant) {
        if (etudiant == null) return null;
        EtudiantDTO dto = EtudiantDTO.builder()
                .id(etudiant.getId())
                .cin(etudiant.getCin())
                .nom(etudiant.getNom())
                .dateNaissance(etudiant.getDateNaissance())
                .email(etudiant.getEmail())
                .anneePremiereInscription(etudiant.getAnneePremiereInscription())
                .age(etudiant.age())
                .build();
        if (etudiant.getDepartement() != null) {
            dto.setDepartementId(etudiant.getDepartement().getId());
            dto.setDepartementNom(etudiant.getDepartement().getNom());
        }
        return dto;
    }

    public Etudiant toEntity(EtudiantDTO dto, Departement departement) {
        if (dto == null) return null;
        return Etudiant.builder()
                .id(dto.getId())
                .cin(dto.getCin())
                .nom(dto.getNom())
                .dateNaissance(dto.getDateNaissance())
                .email(dto.getEmail())
                .anneePremiereInscription(dto.getAnneePremiereInscription())
                .departement(departement)
                .build();
    }
}
