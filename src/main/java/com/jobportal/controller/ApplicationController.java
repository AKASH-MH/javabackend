package com.jobportal.controller;

import com.jobportal.dto.ApplicationDTO;
import com.jobportal.dto.StatusUpdateDTO;
import com.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping
    public ResponseEntity<Page<ApplicationDTO>> getAll(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Page<ApplicationDTO>> getByCandidate(
            @PathVariable Long candidateId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByCandidateId(candidateId, pageable));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<ApplicationDTO>> getByJob(
            @PathVariable Long jobId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.findByJobOpeningId(jobId, pageable));
    }

    @PostMapping
    public ResponseEntity<ApplicationDTO> apply(@Valid @RequestBody ApplicationDTO dto) {
        return new ResponseEntity<>(service.apply(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateDTO statusUpdate) {
        return ResponseEntity.ok(service.updateStatus(id, statusUpdate.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
