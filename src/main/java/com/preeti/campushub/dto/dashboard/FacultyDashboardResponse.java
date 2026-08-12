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
public class FacultyDashboardResponse {

    private String fullName;

    private String employeeId;

    private String email;

    private String designation;

    private String departmentName;

    private Long assignedCourses;

    private Long totalStudents;

    private Long attendanceRecords;

    private Long marksUploaded;
}