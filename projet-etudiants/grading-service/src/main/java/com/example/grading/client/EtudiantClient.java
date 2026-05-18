package com.example.grading.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "etudiant-service", path = "/api")
public interface EtudiantClient {

    @GetMapping("/etudiants/{id}")
    Map<String, Object> getEtudiantById(@PathVariable("id") Long id);
}
