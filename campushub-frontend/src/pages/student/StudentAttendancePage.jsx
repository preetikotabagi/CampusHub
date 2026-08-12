import { useEffect, useState } from "react";
import StudentLayout from "../../components/layout/StudentLayout";
import { getMyAttendance } from "../../services/studentService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { CalendarIcon } from "../../components/icons/Icon";

function StudentAttendancePage() {

    const [attendance, setAttendance] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadAttendance();
    }, []);

    const loadAttendance = async () => {
        try {
            setLoading(true);
            const data = await getMyAttendance();
            setAttendance(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const presentCount = attendance.filter((a) => a.status === "PRESENT").length;
    const attendancePercentage =
        attendance.length === 0
            ? null
            : Math.round((presentCount / attendance.length) * 100);

    return (

        <StudentLayout>

            <PageHeader
                title="My Attendance"
                subtitle="Your attendance record across all enrolled courses."
            />

            {!loading && attendance.length > 0 && (
                <div className="row mb-4">
                    <div className="col-auto">
                        <div className="ch-stat-card" style={{ width: "280px" }}>
                            <div>
                                <p className="ch-stat-card__label">Overall Attendance</p>
                                <h2 className="ch-stat-card__value">
                                    {attendancePercentage}%
                                </h2>
                            </div>

                            <div className="ch-stat-card__icon">
                                <CalendarIcon size={20} />
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {loading ? (
                <LoadingState label="Loading your attendance..." />
            ) : attendance.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={CalendarIcon}
                        title="No attendance records yet"
                        text="Your attendance will show up here once your instructors start marking it."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover mb-0">

                            <thead>

                                <tr>

                                    <th>Course</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th>Remarks</th>

                                </tr>

                            </thead>

                            <tbody>

                                {attendance.map(record => (

                                    <tr key={record.id}>

                                        <td className="fw-semibold">{record.courseName}</td>
                                        <td>{record.attendanceDate}</td>
                                        <td>
                                            <StatusBadge status={record.status} />
                                        </td>
                                        <td className="ch-muted">{record.remarks || "—"}</td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>
                    </div>
                </div>
            )}

        </StudentLayout>

    );
}

export default StudentAttendancePage;
