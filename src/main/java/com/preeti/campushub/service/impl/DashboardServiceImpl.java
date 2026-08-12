package com.preeti.campushub.service.impl;

import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.dashboard.DashboardResponse;
import com.preeti.campushub.repository.AttendanceRepository;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.DepartmentRepository;
import com.preeti.campushub.repository.FacultyRepository;
import com.preeti.campushub.repository.MarksRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    public DashboardServiceImpl(
            DepartmentRepository departmentRepository,
            StudentRepository studentRepository,
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            AttendanceRepository attendanceRepository,
            MarksRepository marksRepository) {

        this.departmentRepository = departmentRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
    }

    @Override
    public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
            .totalDepartments(departmentRepository.countByActiveTrue())
            .totalStudents(studentRepository.countByActiveTrue())
            .totalFaculty(facultyRepository.countByActiveTrue())
            .totalCourses(courseRepository.countByActiveTrue())
            .totalAttendanceRecords(attendanceRepository.countByActiveTrue())
            .totalMarksRecords(marksRepository.countByActiveTrue())
            .build();
        }
}