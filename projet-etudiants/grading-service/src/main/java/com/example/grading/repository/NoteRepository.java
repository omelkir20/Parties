package com.example.grading.repository;

import com.example.grading.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByStudentId(Long studentId);
    List<Note> findByMatiere(String matiere);
}
