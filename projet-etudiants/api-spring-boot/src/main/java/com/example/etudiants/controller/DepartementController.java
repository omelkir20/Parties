package com.example.etudiants.controller;

import com.example.etudiants.dto.DepartementDTO;
import com.example.etudiants.service.DepartementService;
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
@RequestMapping("/api/departements")
@RequiredArgsConstructor
@Tag(name = "Départements", description = "Gestion des départements")
public class DepartementController {

    private final DepartementService departementService;

    @GetMapping
    @Operation(summary = "Lister tous les départements")
    @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    public ResponseEntity<List<DepartementDTO>> findAll() {
        return ResponseEntity.ok(departementService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un département par ID")
    @ApiResponse(responseCode = "200", description = "Département trouvé")
    @ApiResponse(responseCode = "404", description = "Département non trouvé")
    public ResponseEntity<DepartementDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(departementService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un département")
    @ApiResponse(responseCode = "201", description = "Département créé")
    public ResponseEntity<DepartementDTO> create(@Valid @RequestBody DepartementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departementService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un département")
    @ApiResponse(responseCode = "200", description = "Département mis à jour")
    @ApiResponse(responseCode = "404", description = "Département non trouvé")
    public ResponseEntity<DepartementDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody DepartementDTO dto) {
        return ResponseEntity.ok(departementService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un département")
    @ApiResponse(responseCode = "204", description = "Département supprimé")
    @ApiResponse(responseCode = "404", description = "Département non trouvé")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
