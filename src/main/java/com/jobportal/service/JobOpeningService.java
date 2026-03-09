package com.jobportal.service;

import com.jobportal.dto.JobOpeningDTO;
import com.jobportal.entity.JobOpening;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobOpeningService {

    private final JobOpeningRepository repository;

    // ---- READ ----

    @Transactional(readOnly = true)
    public Page<JobOpeningDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public JobOpeningDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<JobOpeningDTO> findByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable).map(this::toDTO);
    }

    // ---- CREATE ----

    public JobOpeningDTO create(JobOpeningDTO dto) {
        JobOpening entity = toEntity(dto);
        return toDTO(repository.save(entity));
    }

    // ---- UPDATE ----

    public JobOpeningDTO update(Long id, JobOpeningDTO dto) {
        JobOpening existing = getOrThrow(id);
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setLocation(dto.getLocation());
        existing.setDepartment(dto.getDepartment());
        existing.setEmploymentType(dto.getEmploymentType());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        return toDTO(repository.save(existing));
    }

    // ---- DELETE ----

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("JobOpening", id);
        }
        repository.deleteById(id);
    }

    // ---- helpers ----

    private JobOpening getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobOpening", id));
    }

    private JobOpeningDTO toDTO(JobOpening e) {
        return JobOpeningDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .location(e.getLocation())
                .department(e.getDepartment())
                .employmentType(e.getEmploymentType())
                .postedDate(e.getPostedDate())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    private JobOpening toEntity(JobOpeningDTO dto) {
        return JobOpening.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .department(dto.getDepartment())
                .employmentType(dto.getEmploymentType())
                .postedDate(dto.getPostedDate() != null ? dto.getPostedDate() : java.time.LocalDate.now())
                .status(dto.getStatus() != null ? dto.getStatus() : "OPEN")
                .build();
    }
}
