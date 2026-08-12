package com.preeti.campushub.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.preeti.campushub.dto.department.DepartmentRequest;
import com.preeti.campushub.dto.department.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    List<DepartmentResponse> getAllDepartments();

    Page<DepartmentResponse> getDepartments(
        int page,
        int size,
        String sortBy,
        String direction);

    List<DepartmentResponse> searchDepartments(String keyword);

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);
}