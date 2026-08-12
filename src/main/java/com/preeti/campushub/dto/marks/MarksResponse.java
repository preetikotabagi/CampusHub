package com.preeti.campushub.dto.marks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarksResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private Integer ia1Marks;
    private Integer ia2Marks;
    private Integer esaMarks;

    private Integer totalMarks;

    private String grade;

    private Integer gradePoint;

    private Boolean active;
}