package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.marks.MarksRequest;
import com.preeti.campushub.dto.marks.MarksResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Marks;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.marks.DuplicateMarksException;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.MarksRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.service.MarksService;

@Service
public class MarksServiceImpl implements MarksService {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public MarksServiceImpl(
            MarksRepository marksRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {

        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public MarksResponse createMarks(MarksRequest request) {

        if (marksRepository.existsByStudentIdAndCourseIdAndActiveTrue(
                request.getStudentId(),
                request.getCourseId())) {

            throw new DuplicateMarksException(
                    "Marks already exist for this student and course");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        int totalMarks =
                request.getIa1Marks()
                + request.getIa2Marks()
                + request.getEsaMarks();

        Marks marks = Marks.builder()
                .student(student)
                .course(course)
                .build();

        marks.setIa1Marks(request.getIa1Marks());
        marks.setIa2Marks(request.getIa2Marks());
        marks.setEsaMarks(request.getEsaMarks());
        applyGrade(marks, totalMarks);

        Marks savedMarks = marksRepository.save(marks);

        return MarksResponse.builder()
                .id(savedMarks.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .courseId(course.getId())
                .courseName(course.getCourseName())
                .ia1Marks(savedMarks.getIa1Marks())
                .ia2Marks(savedMarks.getIa2Marks())
                .esaMarks(savedMarks.getEsaMarks())
                .totalMarks(savedMarks.getTotalMarks())
                .grade(savedMarks.getGrade())
                .gradePoint(savedMarks.getGradePoint())
                .active(savedMarks.getActive())
                .build();
    }

    @Override
    public void saveMarks(List<MarksRequest> requests) {

        for (MarksRequest request : requests) {

            Marks marks = marksRepository.findByStudentIdAndCourseId(
                    request.getStudentId(),
                    request.getCourseId());

            if (marks == null) {

                Student student = studentRepository.findById(request.getStudentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student not found"));

                Course course = courseRepository.findById(request.getCourseId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Course not found"));

                marks = Marks.builder()
                        .student(student)
                        .course(course)
                        .build();
            }

            int totalMarks =
                    request.getIa1Marks()
                    + request.getIa2Marks()
                    + request.getEsaMarks();

            marks.setIa1Marks(request.getIa1Marks());
            marks.setIa2Marks(request.getIa2Marks());
            marks.setEsaMarks(request.getEsaMarks());
            applyGrade(marks, totalMarks);

            marksRepository.save(marks);
        }
    }

    @Override
    public List<MarksResponse> getAllMarks() {

        return marksRepository.findAll()
                .stream()
                .filter(Marks::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MarksResponse getMarksById(Long id) {

        Marks marks = marksRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Marks record not found"));

        return mapToResponse(marks);
    }

    @Override
    public MarksResponse updateMarks(Long id, MarksRequest request) {

        Marks marks = marksRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Marks record not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        int totalMarks =
                request.getIa1Marks()
                + request.getIa2Marks()
                + request.getEsaMarks();

        marks.setStudent(student);
        marks.setCourse(course);
        marks.setIa1Marks(request.getIa1Marks());
        marks.setIa2Marks(request.getIa2Marks());
        marks.setEsaMarks(request.getEsaMarks());
        applyGrade(marks, totalMarks);

        Marks updatedMarks = marksRepository.save(marks);

        return mapToResponse(updatedMarks);
    }

    @Override
    public void deleteMarks(Long id) {

        Marks marks = marksRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Marks record not found"));

        marks.setActive(false);

        marksRepository.save(marks);
    }

    @Override
    public List<MarksResponse> getMyMarks() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return marksRepository.findByStudentEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MarksResponse> getMarksByCourse(Long courseId) {

        return marksRepository
                .findByCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MarksResponse mapToResponse(Marks marks) {

        return MarksResponse.builder()
                .id(marks.getId())
                .studentId(marks.getStudent().getId())
                .studentName(marks.getStudent().getFullName())
                .courseId(marks.getCourse().getId())
                .courseName(marks.getCourse().getCourseName())
                .ia1Marks(marks.getIa1Marks())
                .ia2Marks(marks.getIa2Marks())
                .esaMarks(marks.getEsaMarks())
                .totalMarks(marks.getTotalMarks())
                .grade(marks.getGrade())
                .gradePoint(marks.getGradePoint())
                .active(marks.getActive())
                .build();
    }

    // Was previously copy-pasted three times (createMarks/saveMarks/updateMarks)
    // with identical grading bands - a real risk that a future scale change gets
    // applied in one place and missed in the others. Single source of truth now.
    private void applyGrade(Marks marks, int totalMarks) {

        String grade;
        int gradePoint;

        if (totalMarks >= 90) {
            grade = "S";
            gradePoint = 10;
        } else if (totalMarks >= 80) {
            grade = "A";
            gradePoint = 9;
        } else if (totalMarks >= 70) {
            grade = "B";
            gradePoint = 8;
        } else if (totalMarks >= 60) {
            grade = "C";
            gradePoint = 7;
        } else if (totalMarks >= 55) {
            grade = "D";
            gradePoint = 6;
        } else if (totalMarks >= 50) {
            grade = "E";
            gradePoint = 5;
        } else if (totalMarks >= 40) {
            grade = "F";
            gradePoint = 4;
        } else {
            grade = "X";
            gradePoint = 3;
        }

        marks.setTotalMarks(totalMarks);
        marks.setGrade(grade);
        marks.setGradePoint(gradePoint);
    }
}