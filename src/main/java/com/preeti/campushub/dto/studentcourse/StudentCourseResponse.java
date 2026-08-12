package com.preeti.campushub.dto.studentcourse;

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
public class StudentCourseResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseCode;
    private String courseName;
}