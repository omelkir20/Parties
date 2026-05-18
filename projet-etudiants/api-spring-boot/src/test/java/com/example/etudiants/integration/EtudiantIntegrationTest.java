package com.example.etudiants.integration;

import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.kafka.bootstrap-servers=localhost:9092",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
        })
@AutoConfigureMockMvc
@Testcontainers
class EtudiantIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.cache.type", () -> "none");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @BeforeEach
    void setUp() {
        etudiantRepository.deleteAll();
        departementRepository.deleteAll();
    }

    @Test
    void shouldReturnEmptyListInitially() throws Exception {
        mockMvc.perform(get("/api/etudiants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldPersistAndRetrieveEtudiant() throws Exception {
        Departement dep = departementRepository.save(
                Departement.builder().nom("Informatique").build());

        Etudiant etudiant = Etudiant.builder()
                .cin("TEST001")
                .nom("Integration Test")
                .email("test@example.com")
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .anneePremiereInscription(2022)
                .departement(dep)
                .build();
        etudiantRepository.save(etudiant);

        mockMvc.perform(get("/api/etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Integration Test"))
                .andExpect(jsonPath("$[0].cin").value("TEST001"));
    }

    @Test
    void shouldReturn404ForUnknownEtudiant() throws Exception {
        mockMvc.perform(get("/api/etudiants/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400ForInvalidPayload() throws Exception {
        String invalidJson = """
                {"cin": "", "nom": "", "email": "invalid"}
                """;
        mockMvc.perform(post("/api/etudiants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
