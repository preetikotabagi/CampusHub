import AdminLayout from "../../components/layout/AdminLayout";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import {
    getAttendanceByCourseAndDate
} from "../../services/attendanceService";

import { getAllStudents } from "../../services/studentService";
import { getAllCourses } from "../../services/courseService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { CalendarIcon } from "../../components/icons/Icon";

function AttendancePage() {

    const { courseId } = useParams();

    const [attendanceList, setAttendanceList] = useState([]);

    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadAttendance = async () => {

        try {

            const today = new Date();
            today.setMinutes(today.getMinutes() - today.getTimezoneOffset());

            const attendance = await getAttendanceByCourseAndDate(
                courseId,
                today.toISOString().split("T")[0]
            );

            setAttendanceList(attendance);

        } catch (error) {

            console.error(error);

        }

    };

    const loadStudents = async () => {
        try {
            const students = await getAllStudents();
            setStudents(students.data.data);
        } catch (error) {
            console.error(error);
        }
    };

    const loadCourses = async () => {
        try {
            const courses = await getAllCourses();
            setCourses(courses);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        const load = async () => {
            setLoading(true);
            await Promise.all([loadAttendance(), loadStudents(), loadCourses()]);
            setLoading(false);
        };
        load();
    }, []);

    return (
        <AdminLayout>

            <PageHeader
                title="Attendance Records"
                subtitle="Today's attendance for the selected course."
            />

            {loading ? (
                <LoadingState label="Loading attendance records..." />
            ) : attendanceList.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={CalendarIcon}
                        title="No attendance records found"
                        text="No attendance has been marked for this course today."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover mb-0">

                            <thead>

                                <tr>
                                    <th>Student</th>
                                    <th>Course</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                </tr>

                            </thead>

                            <tbody>

                                {attendanceList.map((attendance) => (

                                    <tr key={attendance.id}>

                                        <td className="fw-semibold">{attendance.studentName}</td>
                                        <td>{attendance.courseName}</td>
                                        <td>{attendance.date}</td>
                                        <td>
                                            <StatusBadge status={attendance.status} />
                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>
                    </div>
                </div>
            )}

        </AdminLayout>
    );
}

export default AttendancePage;
