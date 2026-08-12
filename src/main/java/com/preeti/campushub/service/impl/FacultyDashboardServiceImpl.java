package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.dashboard.FacultyDashboardResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Faculty;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.repository.AttendanceRepository;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.FacultyRepository;
import com.preeti.campushub.repository.MarksRepository;
import com.preeti.campushub.repository.StudentCourseRepository;
import com.preeti.campushub.service.FacultyDashboardService;

@Service
public class FacultyDashboardServiceImpl
        implements FacultyDashboardService {

    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    public FacultyDashboardServiceImpl(
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            StudentCourseRepository studentCourseRepository,
            AttendanceRepository attendanceRepository,
            MarksRepository marksRepository) {

        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.studentCourseRepository = studentCourseRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
    }

    @Override
    public FacultyDashboardResponse getDashboard() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Faculty faculty = facultyRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        List<Course> assignedCourses =
                courseRepository.findByFacultyEmailAndActiveTrue(email);

        long assignedCoursesCount = assignedCourses.size();

        long totalStudents =
                studentCourseRepository
                        .countActiveStudentsByFacultyEmail(email);

        long attendanceRecords = assignedCourses.stream()
                .mapToLong(course ->
                        attendanceRepository
                                .countByCourseIdAndActiveTrue(course.getId()))
                .sum();

        long marksUploaded = assignedCourses.stream()
                .mapToLong(course ->
                        marksRepository
                                .countByCourseIdAndActiveTrue(course.getId()))
                .sum();

        return FacultyDashboardResponse.builder()
                .fullName(faculty.getFullName())
                .employeeId(faculty.getEmployeeId())
                .email(faculty.getEmail())
                .designation(faculty.getDesignation())
                .departmentName(faculty.getDepartment().getName())
                .assignedCourses(assignedCoursesCount)
                .totalStudents(totalStudents)
                .attendanceRecords(attendanceRecords)
                .marksUploaded(marksUploaded)
                .build();
    }
}