package com.preeti.campushub.dto.course;

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
public class CourseResponse {

    private Long id;

    private String courseCode;

    private String courseName;

    private Integer credits;

    private Integer semester;

    private String academicYear;

    private Long departmentId;

    private String departmentName;

    private Long facultyId;

    private String facultyName;

    private Boolean active;
}