package com.example.grading.controller;

import com.example.grading.dto.NoteDTO;
import com.example.grading.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Gestion des notes des étudiants")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    @Operation(summary = "Lister toutes les notes")
    public ResponseEntity<List<NoteDTO>> findAll() {
        return ResponseEntity.ok(noteService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une note par ID")
    public ResponseEntity<NoteDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.findById(id));
    }

    @GetMapping("/etudiant/{studentId}")
    @Operation(summary = "Lister les notes d'un étudiant")
    public ResponseEntity<List<NoteDTO>> findByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(noteService.findByStudentId(studentId));
    }

    @PostMapping
    @Operation(summary = "Créer une note")
    public ResponseEntity<NoteDTO> create(@Valid @RequestBody NoteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une note")
    public ResponseEntity<NoteDTO> update(@PathVariable Long id,
                                           @Valid @RequestBody NoteDTO dto) {
        return ResponseEntity.ok(noteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une note")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
