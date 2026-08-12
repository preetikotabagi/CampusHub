package com.preeti.campushub.dto.analytics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGpaResponse {

    private Long studentId;

    private String studentName;

    private Double cgpa;

    private Integer totalCredits;

    private List<SemesterGpaResponse> semesterGpas;
}
