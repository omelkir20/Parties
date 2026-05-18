package com.example.etudiants.service;

import com.example.etudiants.dto.DepartementDTO;
import com.example.etudiants.entity.Departement;
import com.example.etudiants.exception.ResourceNotFoundException;
import com.example.etudiants.mapper.DepartementMapper;
import com.example.etudiants.repository.DepartementRepository;
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
public class DepartementService {

    private final DepartementRepository departementRepository;
    private final DepartementMapper departementMapper;

    @Cacheable(value = "departements")
    @Transactional(readOnly = true)
    public List<DepartementDTO> findAll() {
        return departementRepository.findAll()
                .stream()
                .map(departementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "departements", key = "#id")
    @Transactional(readOnly = true)
    public DepartementDTO findById(Long id) {
        Departement dep = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'id : " + id));
        return departementMapper.toDTO(dep);
    }

    @CacheEvict(value = "departements", allEntries = true)
    public DepartementDTO save(DepartementDTO dto) {
        Departement dep = departementMapper.toEntity(dto);
        return departementMapper.toDTO(departementRepository.save(dep));
    }

    @CacheEvict(value = "departements", allEntries = true)
    public DepartementDTO update(Long id, DepartementDTO dto) {
        Departement existing = departementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'id : " + id));
        existing.setNom(dto.getNom());
        return departementMapper.toDTO(departementRepository.save(existing));
    }

    @CacheEvict(value = "departements", allEntries = true)
    public void delete(Long id) {
        if (!departementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Département non trouvé avec l'id : " + id);
        }
        departementRepository.deleteById(id);
    }
}
