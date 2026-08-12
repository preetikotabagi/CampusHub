package com.preeti.campushub.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.preeti.campushub.dto.faculty.FacultyRequest;
import com.preeti.campushub.dto.faculty.FacultyResponse;

public interface FacultyService {

    FacultyResponse createFaculty(FacultyRequest request);

    List<FacultyResponse> getAllFaculties();

    FacultyResponse getFacultyById(Long id);

    FacultyResponse updateFaculty(Long id, FacultyRequest request);

    void deleteFaculty(Long id);

    Page<FacultyResponse> getFaculty(
        int page,
        int size,
        String sortBy,
        String direction);

    List<FacultyResponse> searchFaculty(String keyword);

    List<FacultyResponse> filterFaculty(Long departmentId);

    FacultyResponse getMyProfile();
}