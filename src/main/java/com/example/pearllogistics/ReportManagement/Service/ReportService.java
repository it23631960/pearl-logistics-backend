package com.example.pearllogistics.ReportManagement.Service;

import com.example.pearllogistics.ReportManagement.Dto.ReportDto;
import com.example.pearllogistics.ReportManagement.Entity.Report;
import com.example.pearllogistics.ReportManagement.Repository.ReportRepository;
import com.example.pearllogistics.UserManagement.Entity.UserEntity;
import com.example.pearllogistics.UserManagement.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReportDto.ReportResponse saveReport(ReportDto.ReportRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getUserId()));

        Report report = Report.builder()
                .user(user)
                .reportName(request.getReportName())
                .reportType(request.getReportType())
                .data(request.getData())
                .build();

        Report savedReport = reportRepository.save(report);

        return convertToReportResponse(savedReport);
    }

    @Transactional(readOnly = true)
    public List<ReportDto.ReportResponse> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(this::convertToReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReportDto.ReportResponse> getReportsByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return reports.stream()
                .map(this::convertToReportResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportDto.ReportDetailResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        return ReportDto.ReportDetailResponse.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .userName(report.getUser().getFirstName() + " " + report.getUser().getLastName())
                .reportName(report.getReportName())
                .reportType(report.getReportType())
                .data(report.getData())
                .createdAt(report.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteReport(Long reportId) {
        if (!reportRepository.existsById(reportId)) {
            throw new EntityNotFoundException("Report not found with ID: " + reportId);
        }
        reportRepository.deleteById(reportId);
    }

    private ReportDto.ReportResponse convertToReportResponse(Report report) {
        return ReportDto.ReportResponse.builder()
                .id(report.getId())
                .userId(report.getUser().getId())
                .userName(report.getUser().getFirstName() + " " + report.getUser().getLastName())
                .reportName(report.getReportName())
                .reportType(report.getReportType())
                .createdAt(report.getCreatedAt())
                .build();
    }
}