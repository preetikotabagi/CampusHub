package com.preeti.campushub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    long countByActiveTrue();

    Page<Department> findByActiveTrue(Pageable pageable);

    @Query("""
    SELECT d
    FROM Department d
    WHERE d.active = true
    AND (
        LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(d.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    """)
    List<Department> searchDepartments(@Param("keyword") String keyword);
}