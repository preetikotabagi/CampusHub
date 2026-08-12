package com.preeti.campushub.dto.marks;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarksRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "IA1 marks are required")
    @Min(0)
    @Max(25)
    private Integer ia1Marks;

    @NotNull(message = "IA2 marks are required")
    @Min(0)
    @Max(25)
    private Integer ia2Marks;

    @NotNull(message = "ESA marks are required")
    @Min(0)
    @Max(50)
    private Integer esaMarks;
}