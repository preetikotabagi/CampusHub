package com.preeti.campushub.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.preeti.campushub.dto.course.CourseRequest;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.student.StudentResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Department;
import com.preeti.campushub.entity.Faculty;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.entity.StudentCourse;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.course.DuplicateCourseCodeException;
import com.preeti.campushub.exception.course.InvalidFacultyDepartmentException;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.DepartmentRepository;
import com.preeti.campushub.repository.FacultyRepository;
import com.preeti.campushub.repository.StudentCourseRepository;
import com.preeti.campushub.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentCourseRepository studentCourseRepository;

    public CourseServiceImpl(
                CourseRepository courseRepository,
                DepartmentRepository departmentRepository,
                FacultyRepository facultyRepository,
                StudentCourseRepository studentCourseRepository) {

        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.studentCourseRepository = studentCourseRepository;
        }

    @Override
    public CourseResponse createCourse(CourseRequest request) {

        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateCourseCodeException("Course code already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        // Business Rule
        if (!faculty.getDepartment().getId().equals(department.getId())) {
            throw new InvalidFacultyDepartmentException(
                    "Faculty does not belong to the selected department");
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .courseName(request.getCourseName())
                .credits(request.getCredits())
                .semester(request.getSemester())
                .academicYear(request.getAcademicYear())
                .department(department)
                .faculty(faculty)
                .build();

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    @Override
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .filter(Course::getActive)
                .map(this::mapToResponse)
                .toList();
     }

     @Override
        public Page<CourseResponse> getCourses(
                int page,
                int size,
                String sortBy,
                String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return courseRepository
                .findByActiveTrue(PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
        }

        @Override
        public List<CourseResponse> searchCourses(String keyword) {

        return courseRepository.searchCourses(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<CourseResponse> filterCourses(
                Long departmentId,
                Long facultyId) {

        return courseRepository
                .filterCourses(departmentId, facultyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

    @Override
    public CourseResponse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        return mapToResponse(course);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Faculty not found"));

        if (!faculty.getDepartment().getId().equals(department.getId())) {
            throw new IllegalArgumentException(
                    "Faculty does not belong to the selected department");
        }

        if (!course.getCourseCode().equalsIgnoreCase(request.getCourseCode())
                && courseRepository.existsByCourseCode(request.getCourseCode())) {

            throw new DuplicateCourseCodeException("Course code already exists");
        }

        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setCredits(request.getCredits());
        course.setSemester(request.getSemester());
        course.setAcademicYear(request.getAcademicYear());
        course.setDepartment(department);
        course.setFaculty(faculty);

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        course.setActive(false);

        courseRepository.save(course);
    }

    @Override
        public List<CourseResponse> getMyCourses() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return courseRepository
                .findByFacultyEmailAndActiveTrue(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
        }

        @Override
        public List<StudentResponse> getStudentsByCourse(Long courseId) {

                String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

                if (!courseRepository.existsByIdAndFacultyEmailAndActiveTrue(courseId, email)) {
                throw new ResourceNotFoundException("Course not found");
                }

        return studentCourseRepository.findByCourseId(courseId)
                .stream()
                .map(StudentCourse::getStudent)
                .map(this::mapToStudentResponse)
                .toList();
        }

    private CourseResponse mapToResponse(Course course) {

        return CourseResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .semester(course.getSemester())
                .academicYear(course.getAcademicYear())
                .departmentId(course.getDepartment().getId())
                .departmentName(course.getDepartment().getName())
                .facultyId(course.getFaculty().getId())
                .facultyName(course.getFaculty().getFullName())
                .active(course.getActive())
                .build();
    }

    private StudentResponse mapToStudentResponse(Student student) {

        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .usn(student.getUsn())
                .email(student.getEmail())
                .semester(student.getSemester())
                .departmentId(student.getDepartment().getId())
                .departmentName(student.getDepartment().getName())
                .active(student.getActive())
                .build();
    }
}