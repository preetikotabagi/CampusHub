package com.preeti.campushub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseCode(String courseCode);

    boolean existsByCourseCode(String courseCode);

    long countByActiveTrue();

    Page<Course> findByActiveTrue(Pageable pageable);

    @Query("""
    SELECT c
    FROM Course c
    WHERE c.active = true
    AND (
        LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    """)

    List<Course> searchCourses(@Param("keyword") String keyword);

    @Query("""
    SELECT c
    FROM Course c
    WHERE c.active = true
    AND (:departmentId IS NULL OR c.department.id = :departmentId)
    AND (:facultyId IS NULL OR c.faculty.id = :facultyId)
    """)
    List<Course> filterCourses(
            @Param("departmentId") Long departmentId,
            @Param("facultyId") Long facultyId);

    long countByFacultyEmailAndActiveTrue(String email);
    List<Course> findByFacultyEmailAndActiveTrue(String email);

    boolean existsByIdAndFacultyEmailAndActiveTrue(
        Long id,
        String email);
}