package com.example.pearllogistics.ReportManagement.Controller;

import com.example.pearllogistics.ReportManagement.Dto.ReportDto;
import com.example.pearllogistics.ReportManagement.Service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportDto.ReportResponse> createReport(@RequestBody ReportDto.ReportRequest request) {
        ReportDto.ReportResponse response = reportService.saveReport(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDto.ReportResponse>> getAllReports() {
        List<ReportDto.ReportResponse> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDto.ReportResponse>> getReportsByUserId(@PathVariable Long userId) {
        List<ReportDto.ReportResponse> reports = reportService.getReportsByUserId(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDto.ReportDetailResponse> getReportById(@PathVariable Long reportId) {
        ReportDto.ReportDetailResponse report = reportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{reportId}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long reportId) {
        ReportDto.ReportDetailResponse report = reportService.getReportById(reportId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getReportName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(report.getData());
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}