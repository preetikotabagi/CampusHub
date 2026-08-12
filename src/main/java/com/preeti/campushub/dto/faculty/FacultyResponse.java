package com.preeti.campushub.dto.faculty;

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
public class FacultyResponse {

    private Long id;

    private String fullName;

    private String employeeId;

    private String email;

    private String phoneNumber;

    private String designation;

    private Long departmentId;

    private String departmentName;

    private Boolean active;

}