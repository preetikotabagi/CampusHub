package com.preeti.campushub.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.analytics.AttendancePercentageResponse;
import com.preeti.campushub.dto.analytics.AverageMarksResponse;
import com.preeti.campushub.dto.analytics.CourseAverageResponse;
import com.preeti.campushub.dto.analytics.PassPercentageResponse;
import com.preeti.campushub.dto.analytics.SemesterGpaResponse;
import com.preeti.campushub.dto.analytics.StudentGpaResponse;
import com.preeti.campushub.dto.analytics.StudentReportResponse;
import com.preeti.campushub.dto.analytics.TopperResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Marks;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.repository.AttendanceRepository;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.MarksRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.service.AnalyticsService;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StudentRepository studentRepository;
    private final MarksRepository marksRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;

    public AnalyticsServiceImpl(
            MarksRepository marksRepository,
            CourseRepository courseRepository,
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository) {

        this.marksRepository = marksRepository;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @Override
        public List<StudentReportResponse> getStudentReport(Long studentId) {

        return marksRepository.findByStudentIdAndActiveTrue(studentId)
                .stream()
                .map(marks -> StudentReportResponse.builder()
                        .studentName(marks.getStudent().getFullName())
                        .courseName(marks.getCourse().getCourseName())
                        .ia1Marks(marks.getIa1Marks())
                        .ia2Marks(marks.getIa2Marks())
                        .esaMarks(marks.getEsaMarks())
                        .totalMarks(marks.getTotalMarks())
                        .grade(marks.getGrade())
                        .gradePoint(marks.getGradePoint())
                        .build())
                .toList();
        }

    @Override
    public TopperResponse getTopper() {

        Marks topper = marksRepository.findByActiveTrueOrderByTotalMarksDesc()
                .stream()
                .findFirst()
                .orElse(null);

        if (topper == null) {
            return null;
        }

        return TopperResponse.builder()
                .studentName(topper.getStudent().getFullName())
                .courseName(topper.getCourse().getCourseName())
                .totalMarks(topper.getTotalMarks())
                .grade(topper.getGrade())
                .build();
    }

    @Override
    public PassPercentageResponse getPassPercentage() {

        List<Marks> marksList = marksRepository.findByActiveTrueOrderByTotalMarksDesc();

        if (marksList.isEmpty()) {
            return PassPercentageResponse.builder()
                    .passPercentage(0.0)
                    .build();
        }

        long passedStudents = marksList.stream()
                .filter(marks -> marks.getTotalMarks() >= 50)
                .count();

        double percentage = (passedStudents * 100.0) / marksList.size();

        return PassPercentageResponse.builder()
                .passPercentage(percentage)
                .build();
    }

    @Override
    public AverageMarksResponse getAverageMarks() {

        Double average = marksRepository.findAverageMarks();

        if (average == null) {
            average = 0.0;
        }

        return AverageMarksResponse.builder()
                .averageMarks(average)
                .build();
    }

    @Override
    public CourseAverageResponse getCourseAverage(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        Double average = marksRepository.findAverageMarksByCourseId(courseId);

        if (average == null) {
            average = 0.0;
        }

        return CourseAverageResponse.builder()
                .courseName(course.getCourseName())
                .averageMarks(average)
                .build();
    }

    @Override
    public AttendancePercentageResponse getAttendancePercentage(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Long totalClasses =
                attendanceRepository.countAttendanceByStudent(studentId);

        Long presentClasses =
                attendanceRepository.countPresentAttendanceByStudent(studentId);

        double percentage = 0.0;

        if (totalClasses > 0) {
            percentage = (presentClasses * 100.0) / totalClasses;
        }

        return AttendancePercentageResponse.builder()
                .studentName(student.getFullName())
                .attendancePercentage(percentage)
                .build();
    }

    @Override
    public StudentGpaResponse getStudentGpa(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        List<Marks> marksList = marksRepository.findByStudentIdAndActiveTrue(studentId);

        return buildGpaResponse(student, marksList);
    }

    @Override
    public StudentGpaResponse getMyGpa() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        List<Marks> marksList =
                marksRepository.findByStudentIdAndActiveTrue(student.getId());

        return buildGpaResponse(student, marksList);
    }

    // SGPA per semester and overall CGPA, both credit-weighted:
    //     GPA = sum(credits * gradePoint) / sum(credits)
    // This is the standard formula implied by the existing 10-point gradePoint
    // scale in MarksServiceImpl, which was computed but never aggregated into
    // an actual SGPA/CGPA anywhere in the codebase.
    private StudentGpaResponse buildGpaResponse(Student student, List<Marks> marksList) {

        Map<Integer, List<Marks>> bySemester = marksList.stream()
                .collect(Collectors.groupingBy(m -> m.getCourse().getSemester()));

        List<SemesterGpaResponse> semesterGpas = bySemester.entrySet().stream()
                .map(entry -> {

                    int totalCredits = entry.getValue().stream()
                            .mapToInt(m -> m.getCourse().getCredits())
                            .sum();

                    double weightedSum = entry.getValue().stream()
                            .mapToDouble(m -> m.getCourse().getCredits() * m.getGradePoint())
                            .sum();

                    double sgpa = totalCredits == 0 ? 0.0 : weightedSum / totalCredits;

                    return SemesterGpaResponse.builder()
                            .semester(entry.getKey())
                            .sgpa(Math.round(sgpa * 100.0) / 100.0)
                            .totalCredits(totalCredits)
                            .build();
                })
                .sorted(Comparator.comparing(SemesterGpaResponse::getSemester))
                .toList();

        int overallCredits = marksList.stream()
                .mapToInt(m -> m.getCourse().getCredits())
                .sum();

        double overallWeightedSum = marksList.stream()
                .mapToDouble(m -> m.getCourse().getCredits() * m.getGradePoint())
                .sum();

        double cgpa = overallCredits == 0 ? 0.0 : overallWeightedSum / overallCredits;

        return StudentGpaResponse.builder()
                .studentId(student.getId())
                .studentName(student.getFullName())
                .cgpa(Math.round(cgpa * 100.0) / 100.0)
                .totalCredits(overallCredits)
                .semesterGpas(semesterGpas)
                .build();
    }
}