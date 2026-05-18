package com.example.etudiants.controller;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.service.EtudiantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
@Tag(name = "Étudiants", description = "Gestion des étudiants")
public class EtudiantController {

    private final EtudiantService etudiantService;

    @GetMapping
    @Operation(summary = "Lister tous les étudiants (filtrables par année)")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    public ResponseEntity<List<EtudiantDTO>> findAll(
            @RequestParam(required = false) Integer annee) {
        if (annee != null) {
            return ResponseEntity.ok(etudiantService.findByAnnee(annee));
        }
        return ResponseEntity.ok(etudiantService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un étudiant par ID")
    @ApiResponse(responseCode = "200", description = "Étudiant trouvé")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    public ResponseEntity<EtudiantDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.findById(id));
    }

    @GetMapping("/departement/{departementId}")
    @Operation(summary = "Lister les étudiants par département")
    public ResponseEntity<List<EtudiantDTO>> findByDepartement(@PathVariable Long departementId) {
        return ResponseEntity.ok(etudiantService.findByDepartement(departementId));
    }

    @PostMapping
    @Operation(summary = "Créer un étudiant")
    @ApiResponse(responseCode = "201", description = "Étudiant créé")
    public ResponseEntity<EtudiantDTO> create(@Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(etudiantService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un étudiant")
    @ApiResponse(responseCode = "200", description = "Étudiant mis à jour")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    public ResponseEntity<EtudiantDTO> update(@PathVariable Long id,
                                               @Valid @RequestBody EtudiantDTO dto) {
        return ResponseEntity.ok(etudiantService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un étudiant")
    @ApiResponse(responseCode = "204", description = "Étudiant supprimé")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        etudiantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
