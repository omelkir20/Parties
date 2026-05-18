package com.example.etudiants.service;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.kafka.KafkaProducerService;
import com.example.etudiants.mapper.EtudiantMapper;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private DepartementRepository departementRepository;

    @Mock
    private EtudiantMapper etudiantMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private EtudiantService etudiantService;

    @Test
    void shouldReturnAllEtudiants() {
        Etudiant etudiant = buildEtudiant(1L, "Alice");
        EtudiantDTO dto = buildDTO(1L, "Alice");
        when(etudiantRepository.findAll()).thenReturn(List.of(etudiant));
        when(etudiantMapper.toDTO(etudiant)).thenReturn(dto);

        List<EtudiantDTO> result = etudiantService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("Alice");
        verify(etudiantRepository).findAll();
    }

    @Test
    void shouldReturnEtudiantById() {
        Etudiant etudiant = buildEtudiant(1L, "Bob");
        EtudiantDTO dto = buildDTO(1L, "Bob");
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantMapper.toDTO(etudiant)).thenReturn(dto);

        EtudiantDTO result = etudiantService.findById(1L);

        assertThat(result.getNom()).isEqualTo("Bob");
    }

    @Test
    void shouldThrowWhenEtudiantNotFound() {
        when(etudiantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> etudiantService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldSaveAndPublishKafkaEvent() {
        EtudiantDTO dto = buildDTO(null, "Clara");
        Etudiant entity = buildEtudiant(null, "Clara");
        Etudiant saved = buildEtudiant(1L, "Clara");
        EtudiantDTO savedDTO = buildDTO(1L, "Clara");

        when(etudiantMapper.toEntity(any(), any())).thenReturn(entity);
        when(etudiantRepository.save(entity)).thenReturn(saved);
        when(etudiantMapper.toDTO(saved)).thenReturn(savedDTO);
        doNothing().when(kafkaProducerService).publishEtudiantCreated(savedDTO);

        EtudiantDTO result = etudiantService.save(dto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(kafkaProducerService).publishEtudiantCreated(savedDTO);
    }

    @Test
    void shouldDeleteEtudiant() {
        when(etudiantRepository.existsById(1L)).thenReturn(true);
        doNothing().when(etudiantRepository).deleteById(1L);

        etudiantService.delete(1L);

        verify(etudiantRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentEtudiant() {
        when(etudiantRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> etudiantService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldFilterByAnnee() {
        Etudiant e = buildEtudiant(1L, "David");
        EtudiantDTO dto = buildDTO(1L, "David");
        when(etudiantRepository.findByAnneePremiereInscription(2022)).thenReturn(List.of(e));
        when(etudiantMapper.toDTO(e)).thenReturn(dto);

        List<EtudiantDTO> result = etudiantService.findByAnnee(2022);

        assertThat(result).hasSize(1);
    }

    private Etudiant buildEtudiant(Long id, String nom) {
        Etudiant e = new Etudiant();
        e.setId(id);
        e.setNom(nom);
        e.setCin("CIN" + nom);
        e.setEmail(nom.toLowerCase() + "@example.com");
        e.setDateNaissance(LocalDate.of(2000, 1, 1));
        e.setAnneePremiereInscription(2022);
        return e;
    }

    private EtudiantDTO buildDTO(Long id, String nom) {
        return EtudiantDTO.builder()
                .id(id).nom(nom).cin("CIN" + nom)
                .email(nom.toLowerCase() + "@example.com")
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .anneePremiereInscription(2022).build();
    }
}
