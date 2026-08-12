package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.department.DepartmentRequest;
import com.preeti.campushub.dto.department.DepartmentResponse;
import com.preeti.campushub.entity.Department;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.department.DepartmentAlreadyExistsException;
import com.preeti.campushub.repository.DepartmentRepository;
import com.preeti.campushub.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new DepartmentAlreadyExistsException("Department already exists");
        }

        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DepartmentAlreadyExistsException("Department code already exists");
        }

        Department department = Department.builder()
            .name(request.getName())
            .code(request.getCode())
            .description(request.getDescription())
            .build();

        Department savedDepartment = departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .filter(Department::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<DepartmentResponse> getDepartments(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return departmentRepository
                .findByActiveTrue(PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
    }

    @Override
    public List<DepartmentResponse> searchDepartments(String keyword) {

        return departmentRepository.searchDepartments(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        if (!department.getName().equalsIgnoreCase(request.getName())
                && departmentRepository.existsByName(request.getName())) {

            throw new DepartmentAlreadyExistsException("Department already exists");
        }

        if (!department.getCode().equalsIgnoreCase(request.getCode())
                && departmentRepository.existsByCode(request.getCode())) {

            throw new DepartmentAlreadyExistsException("Department code already exists");
        }

        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());

        Department updatedDepartment = departmentRepository.save(department);

        return mapToResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        department.setActive(false);

        departmentRepository.save(department);
    }

    private DepartmentResponse mapToResponse(Department department) {

        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .active(department.getActive())
                .build();
    }
}