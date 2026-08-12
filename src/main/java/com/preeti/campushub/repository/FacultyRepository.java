package com.preeti.campushub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByEmployeeId(String employeeId);

    Optional<Faculty> findByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    long countByActiveTrue();

    Page<Faculty> findByActiveTrue(Pageable pageable);

    @Query("""
    SELECT f
    FROM Faculty f
    WHERE f.active = true
    AND (
    LOWER(f.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(f.employeeId) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(f.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    """)

    List<Faculty> searchFaculty(@Param("keyword") String keyword);

    List<Faculty> findByDepartmentIdAndActiveTrue(Long departmentId);

    List<Faculty> findByActiveTrue();

}