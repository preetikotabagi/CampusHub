package com.preeti.campushub.service;

import java.util.List;

import com.preeti.campushub.dto.studentcourse.StudentCourseRequest;
import com.preeti.campushub.dto.studentcourse.StudentCourseResponse;

public interface StudentCourseService {

    StudentCourseResponse enrollStudent(StudentCourseRequest request);

    List<StudentCourseResponse> getMyCourses();

    void removeEnrollment(Long id);
}