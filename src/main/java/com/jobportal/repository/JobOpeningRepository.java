package com.jobportal.repository;

import com.jobportal.entity.JobOpening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {

    Page<JobOpening> findByStatus(String status, Pageable pageable);

    Page<JobOpening> findByLocationContainingIgnoreCase(String location, Pageable pageable);
}
