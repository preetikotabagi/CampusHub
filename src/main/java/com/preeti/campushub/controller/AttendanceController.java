package com.preeti.campushub.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.attendance.AttendanceRequest;
import com.preeti.campushub.dto.attendance.AttendanceResponse;
import com.preeti.campushub.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PreAuthorize("hasRole('FACULTY')")
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> createAttendance(
            @Valid @RequestBody AttendanceRequest request) {

            System.out.println("Attendance API HIT");

        AttendanceResponse response =
                attendanceService.createAttendance(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance marked successfully",
                        response));
    }

    @PreAuthorize("hasRole('FACULTY')")
        @PostMapping("/bulk")
        public ResponseEntity<ApiResponse<Void>> saveAttendance(
                @Valid @RequestBody List<AttendanceRequest> requests) {

        attendanceService.saveAttendance(requests);

        return ResponseEntity.ok(
                ResponseUtil.success("Attendance saved successfully"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAllAttendance() {

        List<AttendanceResponse> response =
                attendanceService.getAllAttendance();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance records fetched successfully",
                        response));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getAttendanceById(
            @PathVariable Long id) {

        AttendanceResponse response =
                attendanceService.getAttendanceById(id);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance record fetched successfully",
                        response));
    }

    @PreAuthorize("hasRole('FACULTY')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
            @PathVariable Long id,
            @Valid @RequestBody AttendanceRequest request) {

        AttendanceResponse response =
                attendanceService.updateAttendance(id, request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance updated successfully",
                        response));
    }

    @PreAuthorize("hasRole('FACULTY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttendance(
            @PathVariable Long id) {

        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Attendance deleted successfully"));
    }

    @PreAuthorize("hasRole('STUDENT')")
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getMyAttendance() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance fetched successfully",
                        attendanceService.getMyAttendance()));
        }

        @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
        @GetMapping("/course/{courseId}")
        public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByCourseAndDate(
                @PathVariable Long courseId,
                @RequestParam LocalDate attendanceDate) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance fetched successfully",
                        attendanceService.getAttendanceByCourseAndDate(
                                courseId,
                                attendanceDate)));
        }
}