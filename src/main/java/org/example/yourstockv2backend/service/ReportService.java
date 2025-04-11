package org.example.yourstockv2backend.service;

import lombok.AllArgsConstructor;
import org.example.yourstockv2backend.dto.ReportDTO;
import org.example.yourstockv2backend.mapper.ReportMapper;
import org.example.yourstockv2backend.model.Report;
import org.example.yourstockv2backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public ReportDTO createReport(ReportDTO reportDTO) {
        Report report = reportMapper.toEntity(reportDTO);
        report = reportRepository.save(report);
        return reportMapper.toDto(report);
    }

    public ReportDTO getReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        return reportMapper.toDto(report);
    }

    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    public ReportDTO updateReport(Long id, ReportDTO reportDTO) {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        Report updatedReport = reportMapper.toEntity(reportDTO);
        updatedReport.setId(existingReport.getId());
        updatedReport = reportRepository.save(updatedReport);
        return reportMapper.toDto(updatedReport);
    }

    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }
}