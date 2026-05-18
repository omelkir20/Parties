package com.example.etudiants.service;

import com.example.etudiants.dto.EtudiantDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.entity.Etudiant;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.kafka.KafkaProducerService;
import com.example.etudiants.mapper.EtudiantMapper;
import com.example.etudiants.repository.DepartementRepository;
import com.example.etudiants.repository.EtudiantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final DepartementRepository departementRepository;
    private final EtudiantMapper etudiantMapper;
    private final KafkaProducerService kafkaProducerService;

    @Cacheable(value = "etudiants")
    @Transactional(readOnly = true)
    public List<EtudiantDTO> findAll() {
        return etudiantRepository.findAll()
                .stream().map(etudiantMapper::toDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "etudiants", key = "#id")
    @Transactional(readOnly = true)
    public EtudiantDTO findById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouve avec l'id : " + id));
        return etudiantMapper.toDTO(etudiant);
    }

    @Transactional(readOnly = true)
    public List<EtudiantDTO> findByAnnee(int annee) {
        return etudiantRepository.findByAnneePremiereInscription(annee)
                .stream().map(etudiantMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EtudiantDTO> findByDepartement(Long departementId) {
        return etudiantRepository.findByDepartementId(departementId)
                .stream().map(etudiantMapper::toDTO).collect(Collectors.toList());
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public EtudiantDTO save(EtudiantDTO dto) {
        Departement dep = resolveDepartement(dto.getDepartementId());
        Etudiant etudiant = etudiantMapper.toEntity(dto, dep);
        EtudiantDTO saved = etudiantMapper.toDTO(etudiantRepository.save(etudiant));
        kafkaProducerService.publishEtudiantCreated(saved);
        return saved;
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public EtudiantDTO update(Long id, EtudiantDTO dto) {
        Etudiant existing = etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etudiant non trouve avec l'id : " + id));
        Departement dep = resolveDepartement(dto.getDepartementId());
        existing.setCin(dto.getCin());
        existing.setNom(dto.getNom());
        existing.setDateNaissance(dto.getDateNaissance());
        existing.setEmail(dto.getEmail());
        existing.setAnneePremiereInscription(dto.getAnneePremiereInscription());
        existing.setDepartement(dep);
        return etudiantMapper.toDTO(etudiantRepository.save(existing));
    }

    @CacheEvict(value = "etudiants", allEntries = true)
    public void delete(Long id) {
        if (!etudiantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Etudiant non trouve avec l'id : " + id);
        }
        etudiantRepository.deleteById(id);
    }

    private Departement resolveDepartement(Long departementId) {
        if (departementId == null) return null;
        return departementRepository.findById(departementId)
                .orElseThrow(() -> new ResourceNotFoundException("Departement non trouve : " + departementId));
    }
}
