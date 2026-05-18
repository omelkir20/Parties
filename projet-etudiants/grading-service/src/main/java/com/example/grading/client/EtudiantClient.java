package com.example.grading.client;

import com.example.grading.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@Slf4j
public class EtudiantClient {

    @Value("${clients.etudiant-service.url:http://etudiant-service:8081}")
    private String etudiantServiceUrl;

    private final RestClient restClient = RestClient.create();

    @SuppressWarnings("unchecked")
    public Map<String, Object> getEtudiantById(Long id) {
        try {
            return restClient.get()
                    .uri(etudiantServiceUrl + "/api/etudiants/{id}", id)
                    .retrieve()
                    .body(Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Etudiant introuvable avec l'id : " + id);
        } catch (Exception e) {
            log.warn("Impossible de joindre etudiant-service : {}", e.getMessage());
            throw new IllegalStateException("Service etudiant indisponible");
        }
    }
}
