package com.jobportal.repository;

import com.jobportal.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByCandidateId(Long candidateId, Pageable pageable);

    Page<Application> findByJobOpeningId(Long jobOpeningId, Pageable pageable);

    boolean existsByCandidateIdAndJobOpeningId(Long candidateId, Long jobOpeningId);
}
