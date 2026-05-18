package com.example.grading.kafka;

import com.example.grading.dto.NoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, NoteEvent> kafkaTemplate;

    public void publishNoteCreated(NoteDTO note) {
        NoteEvent event = NoteEvent.builder()
                .noteId(note.getId())
                .studentId(note.getStudentId())
                .matiere(note.getMatiere())
                .valeur(note.getValeur())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send("note-created", event);
        log.info("[KAFKA] Evenement publie : note-created pour etudiant ID {}", note.getStudentId());
    }
}
