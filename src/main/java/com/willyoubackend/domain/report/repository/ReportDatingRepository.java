package com.willyoubackend.domain.report.repository;

import com.willyoubackend.domain.report.entity.ReportDating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportDatingRepository extends JpaRepository<ReportDating, Long> {
}
