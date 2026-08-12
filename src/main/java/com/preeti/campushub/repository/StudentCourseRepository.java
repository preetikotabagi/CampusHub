package com.preeti.campushub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.StudentCourse;

public interface StudentCourseRepository
        extends JpaRepository<StudentCourse, Long> {

    @Query("""
        SELECT sc
        FROM StudentCourse sc
        WHERE sc.student.email = :email
        """)
        List<StudentCourse> findByStudentEmail(@Param("email") String email);

    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId);
    List<StudentCourse> findByCourseId(Long courseId);

    @Query("""
        SELECT COUNT(DISTINCT sc.student.id)
        FROM StudentCourse sc
        WHERE sc.course.faculty.email = :facultyEmail
        AND sc.course.active = true
        AND sc.student.active = true
        """)
    long countActiveStudentsByFacultyEmail(
            @Param("facultyEmail") String facultyEmail);
}