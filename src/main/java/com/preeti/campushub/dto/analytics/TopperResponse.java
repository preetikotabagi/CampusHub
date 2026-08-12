package com.preeti.campushub.dto.analytics;

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
public class TopperResponse {

    private String studentName;
    private String courseName;
    private Integer totalMarks;
    private String grade;
}