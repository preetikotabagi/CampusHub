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
import com.preeti.campushub.dto.faculty.FacultyRequest;
import com.preeti.campushub.dto.faculty.FacultyResponse;
import com.preeti.campushub.service.FacultyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<FacultyResponse>> createFaculty(
            @Valid @RequestBody FacultyRequest request) {

        FacultyResponse response = facultyService.createFaculty(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty created successfully",
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FacultyResponse>>> getAllFaculties() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculties fetched successfully",
                        facultyService.getAllFaculties()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/page")
        public ResponseEntity<ApiResponse<Page<FacultyResponse>>> getFaculty(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String direction) {

        Page<FacultyResponse> response =
                facultyService.getFaculty(
                        page,
                        size,
                        sortBy,
                        direction);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty fetched successfully",
                        response));
        }
    
    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<FacultyResponse>>> searchFaculty(
                @RequestParam String keyword) {

        List<FacultyResponse> response =
                facultyService.searchFaculty(keyword);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculties fetched successfully",
                        response));
        }

        @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
        @GetMapping("/filter")
        public ResponseEntity<ApiResponse<List<FacultyResponse>>> filterFaculty(
                @RequestParam(required = false) Long departmentId) {

        List<FacultyResponse> response =
                facultyService.filterFaculty(departmentId);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculties filtered successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyResponse>> getFacultyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty fetched successfully",
                        facultyService.getFacultyById(id)));
    }

    @PreAuthorize("hasRole('FACULTY')")
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<FacultyResponse>> getMyProfile() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty profile fetched successfully",
                        facultyService.getMyProfile()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyResponse>> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody FacultyRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Faculty updated successfully",
                        facultyService.updateFaculty(id, request)));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFaculty(
            @PathVariable Long id) {

        facultyService.deleteFaculty(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Faculty deleted successfully"));
    }
}