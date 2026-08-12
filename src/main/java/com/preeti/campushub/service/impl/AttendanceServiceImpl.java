package com.preeti.campushub.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.attendance.AttendanceRequest;
import com.preeti.campushub.dto.attendance.AttendanceResponse;
import com.preeti.campushub.entity.Attendance;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.exception.attendance.DuplicateAttendanceException;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.repository.AttendanceRepository;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {

        if (attendanceRepository.existsByStudentIdAndCourseIdAndAttendanceDate(
                request.getStudentId(),
                request.getCourseId(),
                request.getAttendanceDate())) {

            throw new DuplicateAttendanceException(
                "Attendance already marked for this date");
            }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        Attendance attendance = Attendance.builder()
                .student(student)
                .course(course)
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus())
                .remarks(request.getRemarks())
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);

        return mapToResponse(savedAttendance);
    }

    @Override
        public void saveAttendance(List<AttendanceRequest> requests) {

        for (AttendanceRequest request : requests) {

                Attendance attendance =
                        attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                                request.getStudentId(),
                                request.getCourseId(),
                                request.getAttendanceDate());

                if (attendance == null) {

                Student student = studentRepository.findById(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student not found"));

                Course course = courseRepository.findById(request.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Course not found"));

                attendance = Attendance.builder()
                        .student(student)
                        .course(course)
                        .attendanceDate(request.getAttendanceDate())
                        .build();
                }

                attendance.setStatus(request.getStatus());
                attendance.setRemarks(request.getRemarks());

                attendanceRepository.save(attendance);
        }
        }

    @Override
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .filter(Attendance::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AttendanceResponse getAttendanceById(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attendance record not found"));

        return mapToResponse(attendance);
    }

    @Override
    public AttendanceResponse updateAttendance(Long id, AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attendance record not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());

        Attendance updatedAttendance = attendanceRepository.save(attendance);

        return mapToResponse(updatedAttendance);
    }

    @Override
    public void deleteAttendance(Long id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Attendance record not found"));

        attendance.setActive(false);

        attendanceRepository.save(attendance);
    }

    @Override
        public List<AttendanceResponse> getMyAttendance() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return attendanceRepository.findByStudentEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<AttendanceResponse> getAttendanceByCourseAndDate(
                Long courseId,
                LocalDate attendanceDate) {

        return attendanceRepository
                .findByCourseIdAndAttendanceDate(courseId, attendanceDate)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        private AttendanceResponse mapToResponse(Attendance attendance) {

                return AttendanceResponse.builder()
                        .id(attendance.getId())
                        .studentId(attendance.getStudent().getId())
                        .studentName(attendance.getStudent().getFullName())
                        .courseId(attendance.getCourse().getId())
                        .courseName(attendance.getCourse().getCourseName())
                        .attendanceDate(attendance.getAttendanceDate())
                        .status(attendance.getStatus())
                        .remarks(attendance.getRemarks())
                        .active(attendance.getActive())
                        .build();
                }
}