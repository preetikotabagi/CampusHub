package com.preeti.campushub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.analytics.AttendancePercentageResponse;
import com.preeti.campushub.dto.analytics.AverageMarksResponse;
import com.preeti.campushub.dto.analytics.CourseAverageResponse;
import com.preeti.campushub.dto.analytics.PassPercentageResponse;
import com.preeti.campushub.dto.analytics.StudentGpaResponse;
import com.preeti.campushub.dto.analytics.StudentReportResponse;
import com.preeti.campushub.dto.analytics.TopperResponse;
import com.preeti.campushub.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/student-report/{studentId}")
    public ResponseEntity<ApiResponse<List<StudentReportResponse>>> getStudentReport(
            @PathVariable Long studentId) {

        List<StudentReportResponse> response =
                analyticsService.getStudentReport(studentId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Student report fetched successfully",
                        response));
    }

    @GetMapping("/topper")
    public ResponseEntity<ApiResponse<TopperResponse>> getTopper() {

        TopperResponse response = analyticsService.getTopper();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Topper fetched successfully",
                        response));
    }

    @GetMapping("/pass-percentage")
    public ResponseEntity<ApiResponse<PassPercentageResponse>> getPassPercentage() {

        PassPercentageResponse response =
                analyticsService.getPassPercentage();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Pass percentage fetched successfully",
                        response));
    }

    @GetMapping("/average-marks")
    public ResponseEntity<ApiResponse<AverageMarksResponse>> getAverageMarks() {

        AverageMarksResponse response =
                analyticsService.getAverageMarks();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Average marks fetched successfully",
                        response));
    }

    @GetMapping("/course-average/{courseId}")
    public ResponseEntity<ApiResponse<CourseAverageResponse>> getCourseAverage(
            @PathVariable Long courseId) {

        CourseAverageResponse response =
                analyticsService.getCourseAverage(courseId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Course average fetched successfully",
                        response));
    }

    @GetMapping("/attendance-percentage/{studentId}")
    public ResponseEntity<ApiResponse<AttendancePercentageResponse>> getAttendancePercentage(
            @PathVariable Long studentId) {

        AttendancePercentageResponse response =
                analyticsService.getAttendancePercentage(studentId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance percentage fetched successfully",
                        response));
    }

    @GetMapping("/sgpa-cgpa/{studentId}")
    public ResponseEntity<ApiResponse<StudentGpaResponse>> getStudentGpa(
            @PathVariable Long studentId) {

        StudentGpaResponse response = analyticsService.getStudentGpa(studentId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "SGPA/CGPA fetched successfully",
                        response));
    }

    // Overrides the class-level ADMIN restriction so a student can see their own
    // SGPA/CGPA (getMyGpa() resolves the student from the authenticated principal,
    // so it can never return another student's data).
    @GetMapping("/my-sgpa-cgpa")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<StudentGpaResponse>> getMyGpa() {

        StudentGpaResponse response = analyticsService.getMyGpa();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "SGPA/CGPA fetched successfully",
                        response));
    }
}