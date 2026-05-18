package com.example.etudiants.kafka;

import com.example.etudiants.dto.EtudiantDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, EtudiantEvent> kafkaTemplate;

    public void publishEtudiantCreated(EtudiantDTO etudiant) {
        EtudiantEvent event = EtudiantEvent.builder()
                .etudiantId(etudiant.getId())
                .nom(etudiant.getNom())
                .email(etudiant.getEmail())
                .timestamp(LocalDateTime.now())
                .build();
        kafkaTemplate.send("etudiant-created", event);
        log.info("[KAFKA] Evenement publie : etudiant-created pour {} (ID: {})",
                etudiant.getNom(), etudiant.getId());
    }
}
