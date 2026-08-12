package com.preeti.campushub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.preeti.campushub.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUsn(String usn);

    Optional<Student> findByEmail(String email);

    boolean existsByUsn(String usn);

    boolean existsByEmail(String email);

    long countByActiveTrue();

    @Query("""
    SELECT s
    FROM Student s
    WHERE s.active = true
    AND (
    LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(s.usn) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    """)

    List<Student> searchStudents(String keyword);

    List<Student> findByDepartmentIdAndSemesterAndActiveTrue(
        Long departmentId,
        Integer semester);
}