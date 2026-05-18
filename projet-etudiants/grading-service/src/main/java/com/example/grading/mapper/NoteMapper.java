package com.example.grading.mapper;

import com.example.grading.dto.NoteDTO;
import com.example.grading.entity.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteDTO toDTO(Note note) {
        if (note == null) return null;
        return NoteDTO.builder()
                .id(note.getId())
                .studentId(note.getStudentId())
                .matiere(note.getMatiere())
                .valeur(note.getValeur())
                .build();
    }

    public Note toEntity(NoteDTO dto) {
        if (dto == null) return null;
        return Note.builder()
                .id(dto.getId())
                .studentId(dto.getStudentId())
                .matiere(dto.getMatiere())
                .valeur(dto.getValeur())
                .build();
    }
}
