package com.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOpeningDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String department;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    private LocalDate postedDate;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
