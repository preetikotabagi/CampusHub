package com.preeti.campushub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.common.ResponseUtil;
import com.preeti.campushub.dto.marks.MarksRequest;
import com.preeti.campushub.dto.marks.MarksResponse;
import com.preeti.campushub.service.MarksService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/marks")
public class MarksController {

    private final MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }

    @PreAuthorize("hasRole('FACULTY')")
    @PostMapping
    public ResponseEntity<ApiResponse<MarksResponse>> createMarks(
            @Valid @RequestBody MarksRequest request) {

        MarksResponse response = marksService.createMarks(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks added successfully",
                        response));
    }

    @PreAuthorize("hasRole('FACULTY')")
        @PostMapping("/bulk")
        public ResponseEntity<ApiResponse<Void>> saveMarks(
                @Valid @RequestBody List<MarksRequest> requests) {

        marksService.saveMarks(requests);

        return ResponseEntity.ok(
                ResponseUtil.success("Marks saved successfully"));
        }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MarksResponse>>> getAllMarks() {

        List<MarksResponse> response = marksService.getAllMarks();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks fetched successfully",
                        response));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MarksResponse>> getMarksById(
            @PathVariable Long id) {

        MarksResponse response = marksService.getMarksById(id);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks fetched successfully",
                        response));
    }
    @PreAuthorize("hasRole('FACULTY')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MarksResponse>> updateMarks(
            @PathVariable Long id,
            @Valid @RequestBody MarksRequest request) {

        MarksResponse response = marksService.updateMarks(id, request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks updated successfully",
                        response));
    }
    @PreAuthorize("hasRole('FACULTY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMarks(
            @PathVariable Long id) {

        marksService.deleteMarks(id);

        return ResponseEntity.ok(
                ResponseUtil.success("Marks deleted successfully"));
    }

    @PreAuthorize("hasAnyRole('ADMIN','FACULTY')")
        @GetMapping("/course/{courseId}")
        public ResponseEntity<ApiResponse<List<MarksResponse>>> getMarksByCourse(
                @PathVariable Long courseId) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks fetched successfully",
                        marksService.getMarksByCourse(courseId)));
    }

    @PreAuthorize("hasRole('STUDENT')")
        @GetMapping("/me")
        public ResponseEntity<ApiResponse<List<MarksResponse>>> getMyMarks() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Marks fetched successfully",
                        marksService.getMyMarks()));
        }
}