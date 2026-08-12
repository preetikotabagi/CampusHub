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
import com.preeti.campushub.dto.department.DepartmentRequest;
import com.preeti.campushub.dto.department.DepartmentResponse;
import com.preeti.campushub.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = departmentService.createDepartment(request);

        return ResponseEntity.ok(
        ResponseUtil.success(
                "Department created successfully",
                response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
        @GetMapping
        public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Departments fetched successfully",
                        departmentService.getAllDepartments()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
        @GetMapping("/page")
        public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> getDepartments(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String direction) {

        Page<DepartmentResponse> response =
                departmentService.getDepartments(
                        page,
                        size,
                        sortBy,
                        direction);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Departments fetched successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
        @GetMapping("/search")
        public ResponseEntity<ApiResponse<List<DepartmentResponse>>> searchDepartments(
                @RequestParam String keyword) {

        List<DepartmentResponse> response =
                departmentService.searchDepartments(keyword);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Departments fetched successfully",
                        response));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY','STUDENT')")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Department fetched successfully",
                        departmentService.getDepartmentById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Department updated successfully",
                        departmentService.updateDepartment(id, request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Department deleted successfully"));
    }
}