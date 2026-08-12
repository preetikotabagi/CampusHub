import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import FacultyLayout from "../../components/layout/FacultyLayout";
import { getStudentsByCourse } from "../../services/courseService";
import {
    saveAttendance,
    getAttendanceByCourseAndDate
} from "../../services/attendanceService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { UsersIcon, SaveIcon } from "../../components/icons/Icon";

function FacultyAttendancePage() {

    const { courseId } = useParams();
    const [students, setStudents] = useState([]);
    const today = new Date();
    today.setMinutes(today.getMinutes() - today.getTimezoneOffset());
    const todayString = today.toISOString().split("T")[0];
    today.setMinutes(today.getMinutes() - today.getTimezoneOffset());

    const [attendanceDate, setAttendanceDate] = useState(todayString);
    const [attendance, setAttendance] = useState({});
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    useEffect(() => {
        loadStudents();
    }, []);

    useEffect(() => {
        loadAttendance();
    }, [attendanceDate]);

    const loadStudents = async () => {
        try {
            setLoading(true);
            const data = await getStudentsByCourse(courseId);
            setStudents(data);
            await loadAttendance();
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const loadAttendance = async () => {

        try {

            const data = await getAttendanceByCourseAndDate(
                courseId,
                attendanceDate
            );

            const attendanceMap = {};

            data.forEach(record => {
                attendanceMap[record.studentId] = record.status;
            });

            setAttendance(attendanceMap);

        } catch (error) {

            console.error(error);

        }

    };

    const handleAttendanceChange = (studentId, status) => {

        setAttendance(prev => ({
            ...prev,
            [studentId]: status
        }));

    };

    const handleSaveAttendance = async () => {

        setSaving(true);
        setFeedback(null);

        try {

            const attendanceList = students.map(student => ({

                studentId: student.id,
                courseId: Number(courseId),
                attendanceDate,
                status: attendance[student.id] || "ABSENT",
                remarks: ""

            }));

            await saveAttendance(attendanceList);

            setFeedback({ type: "success", text: "Attendance saved successfully." });

        } catch (error) {

            console.error(error);
            setFeedback({ type: "danger", text: "Failed to save attendance." });

        } finally {
            setSaving(false);
        }

    };

    return (
        <FacultyLayout>

            <PageHeader
                title="Mark Attendance"
                subtitle="Mark each enrolled student present or absent for the selected date."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            <div className="ch-form-card">
                <label className="form-label ch-required">Attendance date</label>

                <input
                    type="date"
                    className="form-control"
                    style={{ maxWidth: 240 }}
                    value={attendanceDate}
                    onChange={(e) => {
                        if (e.target.value > todayString) {
                            setFeedback({ type: "danger", text: "Future dates are not allowed." });
                            return;
                        }
                        setAttendanceDate(e.target.value);
                    }}
                    max={todayString}
                />
            </div>

            {loading ? (
                <LoadingState label="Loading students..." />
            ) : students.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={UsersIcon}
                        title="No students enrolled"
                        text="No students are enrolled in this course yet."
                    />
                </div>
            ) : (
                <>
                    <div className="ch-table-card">
                        <div className="ch-table-scroll">
                            <table className="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>USN</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Semester</th>
                                        <th>Attendance</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    {students.map(student => (
                                        <tr key={student.id}>
                                            <td className="ch-mono">{student.usn}</td>
                                            <td className="fw-semibold">{student.fullName}</td>
                                            <td>{student.email}</td>
                                            <td>{student.semester}</td>

                                            <td>
                                                <select
                                                    className="form-select"
                                                    style={{ maxWidth: 160 }}
                                                    value={attendance[student.id] || "ABSENT"}
                                                    onChange={(e) =>
                                                        handleAttendanceChange(student.id, e.target.value)
                                                    }
                                                >
                                                    <option value="PRESENT">Present</option>
                                                    <option value="ABSENT">Absent</option>
                                                </select>
                                            </td>

                                        </tr>
                                    ))}
                                </tbody>

                            </table>
                        </div>
                    </div>

                    <div className="mt-3">
                        <button
                            className="btn btn-success d-inline-flex align-items-center gap-2"
                            onClick={handleSaveAttendance}
                            disabled={saving}
                        >
                            <SaveIcon size={15} />
                            {saving ? "Saving..." : "Save Attendance"}
                        </button>
                    </div>
                </>
            )}

        </FacultyLayout>
    );
}

export default FacultyAttendancePage;
