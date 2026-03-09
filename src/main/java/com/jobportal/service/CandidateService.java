package com.jobportal.service;

import com.jobportal.dto.CandidateDTO;
import com.jobportal.entity.Candidate;
import com.jobportal.exception.DuplicateResourceException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService {

    private final CandidateRepository repository;

    // ---- READ ----

    @Transactional(readOnly = true)
    public Page<CandidateDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public CandidateDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    // ---- CREATE ----

    public CandidateDTO create(CandidateDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "A candidate with email '" + dto.getEmail() + "' already exists");
        }
        Candidate entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    // ---- UPDATE ----

    public CandidateDTO update(Long id, CandidateDTO dto) {
        Candidate existing = getOrThrow(id);

        // If email is changing, check uniqueness
        if (!existing.getEmail().equals(dto.getEmail())
                && repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "A candidate with email '" + dto.getEmail() + "' already exists");
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setResume(dto.getResume());
        existing.setPhone(dto.getPhone());
        existing.setSkills(dto.getSkills());
        return toDTO(repository.save(existing));
    }

    // ---- DELETE ----

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate", id);
        }
        repository.deleteById(id);
    }

    // ---- helpers ----

    private Candidate getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
    }

    private CandidateDTO toDTO(Candidate e) {
        return CandidateDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .resume(e.getResume())
                .phone(e.getPhone())
                .skills(e.getSkills())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private Candidate toEntity(CandidateDTO dto) {
        return Candidate.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .resume(dto.getResume())
                .phone(dto.getPhone())
                .skills(dto.getSkills())
                .build();
    }
}
