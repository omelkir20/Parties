package com.example.grading.kafka;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteEvent {
    private Long noteId;
    private Long studentId;
    private String matiere;
    private Double valeur;
    private LocalDateTime timestamp;
}
