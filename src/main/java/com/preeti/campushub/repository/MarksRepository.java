package com.preeti.campushub.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.Marks;

public interface MarksRepository extends JpaRepository<Marks, Long> {

    boolean existsByStudentIdAndCourseIdAndActiveTrue(Long studentId, Long courseId);

    long countByActiveTrue();

    List<Marks> findByStudentIdAndActiveTrue(Long studentId);

    List<Marks> findByActiveTrueOrderByTotalMarksDesc();

    @Query("""
        SELECT AVG(m.totalMarks)
        FROM Marks m
        WHERE m.active = true
        """)
    Double findAverageMarks();

    @Query("""
        SELECT AVG(m.totalMarks)
        FROM Marks m
        WHERE m.course.id = :courseId
        AND m.active = true
        """)
    Double findAverageMarksByCourseId(@Param("courseId") Long courseId);

    @Query("""
        SELECT m
        FROM Marks m
        WHERE m.student.email = :email
        AND m.active = true
        """)
        List<Marks> findByStudentEmail(@Param("email") String email);

    Marks findByStudentIdAndCourseId(
        Long studentId,
        Long courseId);

    List<Marks> findByCourseId(Long courseId);

    long countByCourseIdAndActiveTrue(Long courseId);
}