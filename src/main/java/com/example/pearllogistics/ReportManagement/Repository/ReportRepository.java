package com.example.pearllogistics.ReportManagement.Repository;

import com.example.pearllogistics.ReportManagement.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserId(Long userId);

    List<Report> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Report> findByReportType(String reportType);
}