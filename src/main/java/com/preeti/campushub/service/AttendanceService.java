package com.preeti.campushub.service;

import java.time.LocalDate;
import java.util.List;

import com.preeti.campushub.dto.attendance.AttendanceRequest;
import com.preeti.campushub.dto.attendance.AttendanceResponse;

public interface AttendanceService {

    AttendanceResponse createAttendance(AttendanceRequest request);

    List<AttendanceResponse> getAllAttendance();

    AttendanceResponse getAttendanceById(Long id);

    AttendanceResponse updateAttendance(Long id, AttendanceRequest request);

    void deleteAttendance(Long id);

    List<AttendanceResponse> getMyAttendance();

    void saveAttendance(List<AttendanceRequest> requests);

    List<AttendanceResponse> getAttendanceByCourseAndDate(
        Long courseId,
        LocalDate attendanceDate);

}