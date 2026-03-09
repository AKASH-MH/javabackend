package com.jobportal.service;

import com.jobportal.dto.ApplicationDTO;
import com.jobportal.entity.Application;
import com.jobportal.entity.Candidate;
import com.jobportal.entity.JobOpening;
import com.jobportal.exception.DuplicateResourceException;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.CandidateRepository;
import com.jobportal.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepo;
    private final CandidateRepository candidateRepo;
    private final JobOpeningRepository jobOpeningRepo;

    // ---- READ ----

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> findAll(Pageable pageable) {
        return applicationRepo.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public ApplicationDTO findById(Long id) {
        return toDTO(getOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> findByCandidateId(Long candidateId, Pageable pageable) {
        if (!candidateRepo.existsById(candidateId)) {
            throw new ResourceNotFoundException("Candidate", candidateId);
        }
        return applicationRepo.findByCandidateId(candidateId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ApplicationDTO> findByJobOpeningId(Long jobId, Pageable pageable) {
        if (!jobOpeningRepo.existsById(jobId)) {
            throw new ResourceNotFoundException("JobOpening", jobId);
        }
        return applicationRepo.findByJobOpeningId(jobId, pageable).map(this::toDTO);
    }

    // ---- CREATE (apply) ----

    public ApplicationDTO apply(ApplicationDTO dto) {
        Long candidateId = dto.getCandidateId();
        Long jobId = dto.getJobOpeningId();

        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", candidateId));

        JobOpening job = jobOpeningRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobOpening", jobId));

        if (!"OPEN".equals(job.getStatus())) {
            throw new IllegalArgumentException("Job opening is not accepting applications (status: " + job.getStatus() + ")");
        }

        if (applicationRepo.existsByCandidateIdAndJobOpeningId(candidateId, jobId)) {
            throw new DuplicateResourceException(
                    "Candidate " + candidateId + " has already applied to job " + jobId);
        }

        Application entity = Application.builder()
                .candidate(candidate)
                .jobOpening(job)
                .build();

        return toDTO(applicationRepo.save(entity));
    }

    // ---- UPDATE STATUS ----

    public ApplicationDTO updateStatus(Long id, String newStatus) {
        Application app = getOrThrow(id);
        app.setStatus(newStatus);
        return toDTO(applicationRepo.save(app));
    }

    // ---- DELETE ----

    public void delete(Long id) {
        if (!applicationRepo.existsById(id)) {
            throw new ResourceNotFoundException("Application", id);
        }
        applicationRepo.deleteById(id);
    }

    // ---- helpers ----

    private Application getOrThrow(Long id) {
        return applicationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", id));
    }

    private ApplicationDTO toDTO(Application e) {
        return ApplicationDTO.builder()
                .id(e.getId())
                .candidateId(e.getCandidate().getId())
                .candidateName(e.getCandidate().getName())
                .jobOpeningId(e.getJobOpening().getId())
                .jobTitle(e.getJobOpening().getTitle())
                .status(e.getStatus())
                .appliedDate(e.getAppliedDate())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
