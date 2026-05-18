package com.example.notification.event;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantEvent {
    private Long etudiantId;
    private String nom;
    private String email;
    private LocalDateTime timestamp;
}
