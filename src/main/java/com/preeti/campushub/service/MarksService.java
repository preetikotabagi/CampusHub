package com.preeti.campushub.service;

import java.util.List;

import com.preeti.campushub.dto.marks.MarksRequest;
import com.preeti.campushub.dto.marks.MarksResponse;

public interface MarksService {

    MarksResponse createMarks(MarksRequest request);

    List<MarksResponse> getAllMarks();

    MarksResponse getMarksById(Long id);

    MarksResponse updateMarks(Long id, MarksRequest request);

    void deleteMarks(Long id);

    List<MarksResponse> getMyMarks();

    void saveMarks(List<MarksRequest> requests);

    List<MarksResponse> getMarksByCourse(Long courseId);
}