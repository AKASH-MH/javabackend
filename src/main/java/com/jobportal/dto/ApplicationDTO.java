package com.jobportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {

    private Long id;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    private String candidateName;

    @NotNull(message = "Job Opening ID is required")
    private Long jobOpeningId;

    private String jobTitle;

    private String status;

    private LocalDate appliedDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
