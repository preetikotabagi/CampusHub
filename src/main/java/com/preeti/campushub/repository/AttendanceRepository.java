package com.preeti.campushub.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.preeti.campushub.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByStudentIdAndCourseIdAndAttendanceDate(
            Long studentId,
            Long courseId,
            LocalDate attendanceDate);

    long countByActiveTrue();

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.student.id = :studentId
        AND a.active = true
        """)
    Long countAttendanceByStudent(@Param("studentId") Long studentId);

    @Query("""
        SELECT COUNT(a)
        FROM Attendance a
        WHERE a.student.id = :studentId
        AND a.status = com.preeti.campushub.enums.AttendanceStatus.PRESENT
        AND a.active = true
        """)
    Long countPresentAttendanceByStudent(@Param("studentId") Long studentId);

    @Query("""
        SELECT a
        FROM Attendance a
        WHERE a.student.email = :email
        AND a.active = true
        """)
        List<Attendance> findByStudentEmail(@Param("email") String email);

    Attendance findByStudentIdAndCourseIdAndAttendanceDate(
        Long studentId,
        Long courseId,
        LocalDate attendanceDate);

    List<Attendance> findByCourseIdAndAttendanceDate(
        Long courseId,
        LocalDate attendanceDate);
    
    long countByCourseIdAndActiveTrue(Long courseId);
}