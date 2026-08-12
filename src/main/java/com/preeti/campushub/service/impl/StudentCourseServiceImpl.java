package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.studentcourse.StudentCourseRequest;
import com.preeti.campushub.dto.studentcourse.StudentCourseResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.entity.StudentCourse;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.StudentCourseRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.service.StudentCourseService;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentCourseRepository studentCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseServiceImpl(
            StudentCourseRepository studentCourseRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.studentCourseRepository = studentCourseRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public StudentCourseResponse enrollStudent(StudentCourseRequest request) {

        if (studentCourseRepository.existsByStudentIdAndCourseId(
                request.getStudentId(),
                request.getCourseId())) {

            throw new IllegalArgumentException(
                    "Student is already enrolled in this course.");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        StudentCourse studentCourse = StudentCourse.builder()
                .student(student)
                .course(course)
                .build();

        StudentCourse saved =
                studentCourseRepository.save(studentCourse);

        return mapToResponse(saved);
    }

    @Override
    public List<StudentCourseResponse> getMyCourses() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return studentCourseRepository.findByStudentEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void removeEnrollment(Long id) {

        StudentCourse enrollment =
                studentCourseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment not found"));

        studentCourseRepository.delete(enrollment);
    }

    private StudentCourseResponse mapToResponse(StudentCourse studentCourse) {

        return StudentCourseResponse.builder()
                .id(studentCourse.getId())
                .studentId(studentCourse.getStudent().getId())
                .studentName(studentCourse.getStudent().getFullName())
                .courseId(studentCourse.getCourse().getId())
                .courseCode(studentCourse.getCourse().getCourseCode())
                .courseName(studentCourse.getCourse().getCourseName())
                .build();
    }
}