package com.preeti.campushub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.studentcourse.StudentCourseRequest;
import com.preeti.campushub.dto.studentcourse.StudentCourseResponse;
import com.preeti.campushub.service.StudentCourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/student-courses")
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    public StudentCourseController(StudentCourseService studentCourseService) {
        this.studentCourseService = studentCourseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentCourseResponse>> enrollStudent(
            @Valid @RequestBody StudentCourseRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Student enrolled successfully",
                        studentCourseService.enrollStudent(request)));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentCourseResponse>>> getMyCourses() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        studentCourseService.getMyCourses()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removeEnrollment(
            @PathVariable Long id) {

        studentCourseService.removeEnrollment(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Enrollment removed successfully"));
    }
}