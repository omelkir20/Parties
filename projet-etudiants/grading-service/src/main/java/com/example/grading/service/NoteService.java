package com.example.grading.service;

import com.example.grading.client.EtudiantClient;
import com.example.grading.dto.NoteDTO;
import com.example.grading.entity.Note;
import com.example.grading.exception.ResourceNotFoundException;
import com.example.grading.mapper.NoteMapper;
import com.example.grading.repository.NoteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final EtudiantClient etudiantClient;

    @Transactional(readOnly = true)
    public List<NoteDTO> findAll() {
        return noteRepository.findAll().stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NoteDTO findById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'id : " + id));
        return noteMapper.toDTO(note);
    }

    @Transactional(readOnly = true)
    public List<NoteDTO> findByStudentId(Long studentId) {
        return noteRepository.findByStudentId(studentId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NoteDTO save(NoteDTO dto) {
        verifierExistenceEtudiant(dto.getStudentId());
        Note note = noteMapper.toEntity(dto);
        return noteMapper.toDTO(noteRepository.save(note));
    }

    public NoteDTO update(Long id, NoteDTO dto) {
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note non trouvée avec l'id : " + id));
        verifierExistenceEtudiant(dto.getStudentId());
        existing.setStudentId(dto.getStudentId());
        existing.setMatiere(dto.getMatiere());
        existing.setValeur(dto.getValeur());
        return noteMapper.toDTO(noteRepository.save(existing));
    }

    public void delete(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note non trouvée avec l'id : " + id);
        }
        noteRepository.deleteById(id);
    }

    private void verifierExistenceEtudiant(Long studentId) {
        try {
            etudiantClient.getEtudiantById(studentId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Étudiant introuvable avec l'id : " + studentId);
        }
    }
}
