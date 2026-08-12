package com.preeti.campushub.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.preeti.campushub.dto.attendance.AttendanceResponse;
import com.preeti.campushub.dto.course.CourseResponse;
import com.preeti.campushub.dto.marks.MarksResponse;
import com.preeti.campushub.dto.student.StudentRequest;
import com.preeti.campushub.dto.student.StudentResponse;
import com.preeti.campushub.entity.Course;
import com.preeti.campushub.entity.Department;
import com.preeti.campushub.entity.Student;
import com.preeti.campushub.entity.StudentCourse;
import com.preeti.campushub.entity.User;
import com.preeti.campushub.enums.Role;
import com.preeti.campushub.exception.common.DuplicateEmailException;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.student.DuplicateUsnException;
import com.preeti.campushub.exception.student.InvalidProfilePictureException;
import com.preeti.campushub.repository.AttendanceRepository;
import com.preeti.campushub.repository.CourseRepository;
import com.preeti.campushub.repository.DepartmentRepository;
import com.preeti.campushub.repository.MarksRepository;
import com.preeti.campushub.repository.StudentCourseRepository;
import com.preeti.campushub.repository.StudentRepository;
import com.preeti.campushub.repository.UserRepository;
import com.preeti.campushub.service.EmailService;
import com.preeti.campushub.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final StudentCourseRepository studentCourseRepository;

        @Value("${file.upload-dir}")
    private String uploadDir;

    public StudentServiceImpl(
                StudentRepository studentRepository,
                DepartmentRepository departmentRepository,
                UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                EmailService emailService,
                CourseRepository courseRepository,
                AttendanceRepository attendanceRepository,
                MarksRepository marksRepository,
                StudentCourseRepository studentCourseRepository) {

        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.courseRepository = courseRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
        this.studentCourseRepository = studentCourseRepository;
        }

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByUsn(request.getUsn())) {
            throw new DuplicateUsnException("USN already exists");
        }

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        String temporaryPassword = "Student@123";

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber("9" + System.currentTimeMillis())
                .password(passwordEncoder.encode(temporaryPassword))
                .role(Role.STUDENT)
                .passwordChanged(false)
                .build();

        userRepository.save(user);

                emailService.sendEmail(
                request.getEmail(),
                "CampusHub Student Account Created",
                "Welcome to CampusHub!\n\n"
                        + "Email: " + request.getEmail() + "\n"
                        + "Temporary Password: " + temporaryPassword + "\n\n"
                        + "Please login and change your password immediately."
        );

        Student student = Student.builder()
                .fullName(request.getFullName())
                .usn(request.getUsn())
                .email(request.getEmail())
                .semester(request.getSemester())
                .department(department)
                .build();

        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    @Override
    public List<StudentResponse> getAllStudents() {

        return studentRepository.findAll()
                .stream()
                .filter(Student::getActive)
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<StudentResponse> getStudents(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return studentRepository
                .findAll(PageRequest.of(page, size, sort))
                .map(student -> StudentResponse.builder()
                        .id(student.getId())
                        .usn(student.getUsn())
                        .fullName(student.getFullName())
                        .email(student.getEmail())
                        .semester(student.getSemester())
                        .departmentId(student.getDepartment().getId())
                        .departmentName(student.getDepartment().getName())
                        .active(student.getActive())
                        .build());
    }

    @Override
    public List<StudentResponse> searchStudents(String keyword) {

        return studentRepository.searchStudents(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> filterStudents(
            Long departmentId,
            Integer semester) {

        return studentRepository
                .findByDepartmentIdAndSemesterAndActiveTrue(
                        departmentId,
                        semester)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StudentResponse getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        return mapToResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found"));

        if (!student.getUsn().equalsIgnoreCase(request.getUsn())
                && studentRepository.existsByUsn(request.getUsn())) {

            throw new DuplicateUsnException("USN already exists");
        }

        if (!student.getEmail().equalsIgnoreCase(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateEmailException("Email already exists");
        }
        
        student.setFullName(request.getFullName());
        student.setUsn(request.getUsn());
        student.setEmail(request.getEmail());
        student.setSemester(request.getSemester());
        student.setDepartment(department);

        Student updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        student.setActive(false);

        studentRepository.save(student);
    }

    @Override
        public StudentResponse uploadProfilePicture(Long studentId, MultipartFile file) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

                if (file.isEmpty()) {
                        throw new InvalidProfilePictureException("Please select an image.");
                        }

                        String contentType = file.getContentType();

                        if (contentType == null ||
                                !(contentType.equals("image/jpeg")
                                || contentType.equals("image/png")
                                || contentType.equals("image/jpg"))) {

                        throw new InvalidProfilePictureException("Only JPG, JPEG and PNG images are allowed.");
                        }

                        if (file.getSize() > 2 * 1024 * 1024) {
                        throw new InvalidProfilePictureException("Maximum allowed file size is 2 MB."); 
                        }    

        try {

                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                }

                String originalFileName = file.getOriginalFilename();

                String extension = originalFileName.substring(
                        originalFileName.lastIndexOf("."));

                String fileName = studentId + "_"
                        + UUID.randomUUID()
                        + extension;

                Path filePath = uploadPath.resolve(fileName);

                if (student.getProfilePicture() != null) {

                        Path oldFile = Paths.get(
                                student.getProfilePicture().replaceFirst("/", ""));

                        Files.deleteIfExists(oldFile);
                }

                Files.copy(
                        file.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                student.setProfilePicture(
                        "/uploads/profile-pictures/" + fileName);

                Student updatedStudent = studentRepository.save(student);

                return mapToResponse(updatedStudent);

        } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture");
        }
        }

    @Override
        public StudentResponse getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        return mapToResponse(student);
        }

        @Override
        public Resource downloadProfilePicture(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found"));

        if (student.getProfilePicture() == null) {
                throw new ResourceNotFoundException("Profile picture not found");
        }

        try {

                Path path = Paths.get(student.getProfilePicture().replaceFirst("/", ""));

                Resource resource = new UrlResource(path.toUri());

                if (!resource.exists()) {
                throw new ResourceNotFoundException("Profile picture not found");
                }

                return resource;

        } catch (Exception e) {
                throw new RuntimeException("Failed to load profile picture");
        }
        }

    @Override
        public List<CourseResponse> getMyCourses() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        List<StudentCourse> studentCourses =
        studentCourseRepository.findByStudentEmail(email);

        System.out.println("Logged in email: " + email);
        System.out.println("Student Courses Count: " + studentCourses.size());

        return studentCourses
                .stream()
                .map(studentCourse -> {

                        Course course = studentCourse.getCourse();

                        return CourseResponse.builder()
                                .id(course.getId())
                                .courseCode(course.getCourseCode())
                                .courseName(course.getCourseName())
                                .credits(course.getCredits())
                                .departmentId(course.getDepartment().getId())
                                .departmentName(course.getDepartment().getName())
                                .facultyId(course.getFaculty().getId())
                                .facultyName(course.getFaculty().getFullName())
                                .active(course.getActive())
                                .build();
                })
                .toList();
        }

        @Override
        public List<AttendanceResponse> getMyAttendance() {

                String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

                return attendanceRepository
                        .findByStudentEmail(email)
                        .stream()
                        .map(attendance -> AttendanceResponse.builder()
                                .id(attendance.getId())
                                .studentId(attendance.getStudent().getId())
                                .studentName(attendance.getStudent().getFullName())
                                .courseId(attendance.getCourse().getId())
                                .courseName(attendance.getCourse().getCourseName())
                                .attendanceDate(attendance.getAttendanceDate())
                                .status(attendance.getStatus())
                                .remarks(attendance.getRemarks())
                                .active(attendance.getActive())
                                .build())
                        .toList();
        }

        @Override
        public List<MarksResponse> getMyMarks() {

                String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

                return marksRepository
                        .findByStudentEmail(email)
                        .stream()
                        .map(mark -> MarksResponse.builder()
                                .id(mark.getId())
                                .studentId(mark.getStudent().getId())
                                .studentName(mark.getStudent().getFullName())
                                .courseId(mark.getCourse().getId())
                                .courseName(mark.getCourse().getCourseName())
                                .ia1Marks(mark.getIa1Marks())
                                .ia2Marks(mark.getIa2Marks())
                                .esaMarks(mark.getEsaMarks())
                                .totalMarks(mark.getTotalMarks())
                                .grade(mark.getGrade())
                                .gradePoint(mark.getGradePoint())
                                .active(mark.getActive())
                                .build())
                        .toList();
                }

    private StudentResponse mapToResponse(Student student) {

        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .usn(student.getUsn())
                .email(student.getEmail())
                .profilePicture(student.getProfilePicture())
                .semester(student.getSemester())
                .departmentId(student.getDepartment().getId())
                .departmentName(student.getDepartment().getName())
                .active(student.getActive())
                .build();
    }
}