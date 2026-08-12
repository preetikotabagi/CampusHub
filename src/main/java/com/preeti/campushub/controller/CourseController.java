package com.preeti.campushub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
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
import com.preeti.campushub.dto.course.CourseRequest;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.student.StudentResponse;
import com.preeti.campushub.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CourseRequest request) {

        CourseResponse response = courseService.createCourse(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Course created successfully",
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/page")
        public ResponseEntity<ApiResponse<Page<CourseResponse>>> getCourses(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String direction) {

        Page<CourseResponse> response =
                courseService.getCourses(
                        page,
                        size,
                        sortBy,
                        direction);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAllCourses() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        courseService.getAllCourses()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<CourseResponse>>> searchCourses(
                @RequestParam String keyword) {

        List<CourseResponse> response =
                courseService.searchCourses(keyword);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/filter")
        public ResponseEntity<ApiResponse<List<CourseResponse>>> filterCourses(
                @RequestParam(required = false) Long departmentId,
                @RequestParam(required = false) Long facultyId) {

        List<CourseResponse> response =
                courseService.filterCourses(departmentId, facultyId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses filtered successfully",
                        response));
        }

    @PreAuthorize("hasRole('FACULTY')")
        @GetMapping("/my-courses")
        public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        courseService.getMyCourses()));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Course fetched successfully",
                        courseService.getCourseById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Course updated successfully",
                        courseService.updateCourse(id, request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Course deleted successfully"));
    }

    @PreAuthorize("hasRole('FACULTY')")
        @GetMapping("/{courseId}/students")
        public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsByCourse(
                @PathVariable Long courseId) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Students fetched successfully",
                        courseService.getStudentsByCourse(courseId)));
    }
}