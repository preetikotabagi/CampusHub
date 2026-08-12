package com.preeti.campushub.dto.attendance;

import java.time.LocalDate;

import com.preeti.campushub.enums.AttendanceStatus;

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
public class AttendanceResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private String remarks;

    private Boolean active;
}