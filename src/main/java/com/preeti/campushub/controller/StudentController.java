package com.preeti.campushub.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.attendance.AttendanceResponse;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.marks.MarksResponse;
import com.preeti.campushub.dto.student.StudentRequest;
import com.preeti.campushub.dto.student.StudentResponse;
import com.preeti.campushub.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> createStudent(
            @Valid @RequestBody StudentRequest request) {

        StudentResponse response = studentService.createStudent(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Student created successfully",
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAllStudents() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Students fetched successfully",
                        studentService.getAllStudents()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping("/page")
        public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudents(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String direction) {

        Page<StudentResponse> response =
                studentService.getStudents(
                        page,
                        size,
                        sortBy,
                        direction);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Students fetched successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudents(
                @RequestParam String keyword) {

        List<StudentResponse> response =
                studentService.searchStudents(keyword);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Students fetched successfully",
                        response));
        }
    
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping("/filter")
        public ResponseEntity<ApiResponse<List<StudentResponse>>> filterStudents(
                @RequestParam Long departmentId,
                @RequestParam Integer semester) {

        List<StudentResponse> response =
                studentService.filterStudents(
                        departmentId,
                        semester);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Students fetched successfully",
                        response));
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/{id}/profile-picture")
        public ResponseEntity<ApiResponse<StudentResponse>> uploadProfilePicture(
                @PathVariable Long id,
                @RequestParam("file") MultipartFile file) {

        StudentResponse response =
                studentService.uploadProfilePicture(id, file);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Profile picture uploaded successfully",
                        response));
        }

        @GetMapping("/{id}/profile-picture")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<Resource> downloadProfilePicture(
                @PathVariable Long id) {

        Resource resource = studentService.downloadProfilePicture(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Student updated successfully",
                        studentService.updateStudent(id, request)));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Student deleted successfully"));
    }

    @GetMapping("/me")
        @PreAuthorize("hasRole('STUDENT')")
        public ResponseEntity<ApiResponse<StudentResponse>> getMyProfile() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Profile fetched successfully",
                        studentService.getMyProfile()));
    }

    @GetMapping("/my-courses")
        @PreAuthorize("hasRole('STUDENT')")
        public ResponseEntity<ApiResponse<List<CourseResponse>>> getMyCourses() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Courses fetched successfully",
                        studentService.getMyCourses()));
    }

    @GetMapping("/my-attendance")
        @PreAuthorize("hasRole('STUDENT')")
        public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getMyAttendance() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Attendance fetched successfully",
                        studentService.getMyAttendance()));
    }

    @GetMapping("/my-marks")
        @PreAuthorize("hasRole('STUDENT')")
                public ResponseEntity<ApiResponse<List<MarksResponse>>> getMyMarks() {

                return ResponseEntity.ok(
                        ResponseUtil.success(
                                "Marks fetched successfully",
                                studentService.getMyMarks()));
                }
}