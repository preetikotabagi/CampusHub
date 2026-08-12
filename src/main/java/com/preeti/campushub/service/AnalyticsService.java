package com.preeti.campushub.service;

import java.util.List;

import com.preeti.campushub.dto.analytics.AttendancePercentageResponse;
import com.preeti.campushub.dto.analytics.AverageMarksResponse;
import com.preeti.campushub.dto.analytics.CourseAverageResponse;
import com.preeti.campushub.dto.analytics.PassPercentageResponse;
import com.preeti.campushub.dto.analytics.StudentGpaResponse;
import com.preeti.campushub.dto.analytics.StudentReportResponse;
import com.preeti.campushub.dto.analytics.TopperResponse;

public interface AnalyticsService {

    List<StudentReportResponse> getStudentReport(Long studentId);

    TopperResponse getTopper();

    PassPercentageResponse getPassPercentage();

    AverageMarksResponse getAverageMarks();

    CourseAverageResponse getCourseAverage(Long courseId);

    AttendancePercentageResponse getAttendancePercentage(Long studentId);

    StudentGpaResponse getStudentGpa(Long studentId);

    StudentGpaResponse getMyGpa();
}