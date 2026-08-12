package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.faculty.FacultyRequest;
import com.preeti.campushub.dto.faculty.FacultyResponse;
import com.preeti.campushub.entity.Department;
import com.preeti.campushub.entity.Faculty;
import com.preeti.campushub.entity.User;
import com.preeti.campushub.enums.Role;
import com.preeti.campushub.exception.common.DuplicateEmailException;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.faculty.DuplicateEmployeeIdException;
import com.preeti.campushub.repository.DepartmentRepository;
import com.preeti.campushub.repository.FacultyRepository;
import com.preeti.campushub.repository.UserRepository;
import com.preeti.campushub.service.EmailService;
import com.preeti.campushub.service.FacultyService;
import com.preeti.campushub.util.PasswordGenerator;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public FacultyServiceImpl(
            FacultyRepository facultyRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public FacultyResponse createFaculty(FacultyRequest request) {
        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateEmployeeIdException("Employee ID already exists");
        }

        if (facultyRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        if (facultyRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateEmailException("Phone Number already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("User email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateEmailException("User phone number already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        Faculty faculty = Faculty.builder()
            .fullName(request.getFullName())
            .employeeId(request.getEmployeeId())
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .designation(request.getDesignation())
            .department(department)
            .build();

        String generatedPassword = PasswordGenerator.generatePassword(10);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(generatedPassword))
                .role(Role.FACULTY)
                .build();

        userRepository.save(user);
        
        Faculty savedFaculty = facultyRepository.save(faculty);

        emailService.sendEmail(
                request.getEmail(),
                "Welcome to CampusHub",
                """
                Hello %s,

                Your CampusHub faculty account has been created successfully.

                Email: %s
                Password: %s

                Please login and change your password after your first login.

                Regards,
                CampusHub Team
                """.formatted(
                        request.getFullName(),
                        request.getEmail(),
                        generatedPassword
                )
        );

        return mapToResponse(savedFaculty);
    }

    @Override
    public List<FacultyResponse> getAllFaculties() {

        return facultyRepository.findAll()
                .stream()
                .filter(Faculty::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<FacultyResponse> getFaculty(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return facultyRepository
                .findByActiveTrue(PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
    }

    @Override
    public List<FacultyResponse> searchFaculty(String keyword) {

        return facultyRepository.searchFaculty(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FacultyResponse getFacultyById(Long id) {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        return mapToResponse(faculty);
    }

    @Override
    public FacultyResponse updateFaculty(Long id, FacultyRequest request) {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        if (!faculty.getEmployeeId().equalsIgnoreCase(request.getEmployeeId())
                && facultyRepository.existsByEmployeeId(request.getEmployeeId())) {

            throw new DuplicateEmployeeIdException("Employee ID already exists");
        }

        if (!faculty.getEmail().equalsIgnoreCase(request.getEmail())
                && facultyRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateEmailException("Email already exists");
        }

        faculty.setFullName(request.getFullName());
        faculty.setEmployeeId(request.getEmployeeId());
        faculty.setEmail(request.getEmail());
        faculty.setDesignation(request.getDesignation());
        faculty.setDepartment(department);

        Faculty updatedFaculty = facultyRepository.save(faculty);

        return mapToResponse(updatedFaculty);
    }

    @Override
    public FacultyResponse getMyProfile() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Faculty faculty = facultyRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        return mapToResponse(faculty);
    }

    @Override
    public void deleteFaculty(Long id) {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        faculty.setActive(false);

        facultyRepository.save(faculty);
    }

    private FacultyResponse mapToResponse(Faculty faculty) {

        return FacultyResponse.builder()
            .id(faculty.getId())
            .fullName(faculty.getFullName())
            .employeeId(faculty.getEmployeeId())
            .email(faculty.getEmail())
            .phoneNumber(faculty.getPhoneNumber())
            .designation(faculty.getDesignation())
            .departmentId(faculty.getDepartment().getId())
            .departmentName(faculty.getDepartment().getName())
            .active(faculty.getActive())
            .build();
    }

    @Override
    public List<FacultyResponse> filterFaculty(Long departmentId) {

        List<Faculty> faculties;

        if (departmentId != null) {
            faculties = facultyRepository.findByDepartmentIdAndActiveTrue(departmentId);
        } else {
            faculties = facultyRepository.findByActiveTrue();
        }

        return faculties.stream()
                .map(this::mapToResponse)
                .toList();
    }
}