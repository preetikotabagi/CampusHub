package com.preeti.campushub.dto.student;

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
public class StudentResponse {

    private Long id;

    private String fullName;

    private String usn;

    private String email;

    private String profilePicture;

    private Integer semester;

    private Long departmentId;

    private String departmentName;

    private Boolean active;
}