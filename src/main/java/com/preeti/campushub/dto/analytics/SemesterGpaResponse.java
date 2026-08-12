package com.preeti.campushub.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterGpaResponse {

    private Integer semester;

    private Double sgpa;

    private Integer totalCredits;
}
