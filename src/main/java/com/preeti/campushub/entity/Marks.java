package com.preeti.campushub.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "marks")
public class Marks extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Integer ia1Marks;

    @Column(nullable = false)
    private Integer ia2Marks;

    @Column(nullable = false)
    private Integer esaMarks;

    @Column(nullable = false)
    private Integer totalMarks;

    @Column(nullable = false)
    private String grade;

    @Column(nullable = false)
    private Integer gradePoint;
}