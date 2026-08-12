package com.preeti.campushub.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.preeti.campushub.dto.attendance.AttendanceResponse;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.marks.MarksResponse;
import com.preeti.campushub.dto.student.StudentRequest;
import com.preeti.campushub.dto.student.StudentResponse;

public interface StudentService {

    StudentResponse createStudent(StudentRequest request);

    List<StudentResponse> getAllStudents();

    StudentResponse getStudentById(Long id);

    StudentResponse updateStudent(Long id, StudentRequest request);

    void deleteStudent(Long id);

    Page<StudentResponse> getStudents(
        int page,
        int size,
        String sortBy,
        String direction);

    List<StudentResponse> searchStudents(String keyword);

    List<StudentResponse> filterStudents(
        Long departmentId,
        Integer semester);

    StudentResponse uploadProfilePicture(Long studentId, MultipartFile file);

    StudentResponse getMyProfile();

    Resource downloadProfilePicture(Long id);

    List<CourseResponse> getMyCourses();

    List<AttendanceResponse> getMyAttendance();

    List<MarksResponse> getMyMarks();
} 