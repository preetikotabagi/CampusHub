package com.preeti.campushub.dto.dashboard;

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
public class DashboardResponse {

    private Long totalDepartments;

    private Long totalStudents;

    private Long totalFaculty;

    private Long totalCourses;

    private Long totalAttendanceRecords;

    private Long totalMarksRecords;
}