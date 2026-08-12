package com.preeti.campushub.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.preeti.campushub.dto.course.CourseRequest;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.student.StudentResponse;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    List<CourseResponse> getAllCourses();

    Page<CourseResponse> getCourses(
        int page,
        int size,
        String sortBy,
        String direction);

    List<CourseResponse> searchCourses(String keyword);

    List<CourseResponse> filterCourses(
        Long departmentId,
        Long facultyId);

    CourseResponse getCourseById(Long id);

    CourseResponse updateCourse(Long id, CourseRequest request);

    void deleteCourse(Long id);

    List<CourseResponse> getMyCourses();

    List<StudentResponse> getStudentsByCourse(Long courseId);

}