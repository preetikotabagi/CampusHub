package com.preeti.campushub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.dashboard.FacultyDashboardResponse;
import com.preeti.campushub.service.FacultyDashboardService;

@RestController
@RequestMapping("/api/faculty/dashboard")
public class FacultyDashboardController {

    private final FacultyDashboardService facultyDashboardService;

    public FacultyDashboardController(
            FacultyDashboardService facultyDashboardService) {

        this.facultyDashboardService = facultyDashboardService;
    }

    @PreAuthorize("hasRole('FACULTY')")
    @GetMapping
    public ResponseEntity<ApiResponse<FacultyDashboardResponse>> getDashboard() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty dashboard fetched successfully",
                        facultyDashboardService.getDashboard()));
    }
}