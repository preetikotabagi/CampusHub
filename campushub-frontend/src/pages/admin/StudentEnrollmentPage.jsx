import { useEffect, useState } from "react";
import AdminLayout from "../../components/layout/AdminLayout";
import { fetchStudents } from "../../services/studentService";
import { getAllCourses } from "../../services/courseService";
import { enrollStudent } from "../../services/studentCourseService";
import PageHeader from "../../components/common/PageHeader";
import { ClipboardListIcon } from "../../components/icons/Icon";

function StudentEnrollmentPage() {

    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [studentId, setStudentId] = useState("");
    const [courseId, setCourseId] = useState("");
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {

        try {

            const studentData = await fetchStudents();
            const courseData = await getAllCourses();

            setStudents(studentData);
            setCourses(courseData);

        } catch (error) {
            console.error(error);
        }

    };

    const handleEnrollment = async () => {

        if (!studentId || !courseId) {
            setFeedback({ type: "danger", text: "Please select both a student and a course." });
            return;
        }

        setSaving(true);
        setFeedback(null);

        try {

            await enrollStudent({
                studentId,
                courseId
            });

            setFeedback({ type: "success", text: "Student enrolled successfully." });
            setStudentId("");
            setCourseId("");

        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Enrollment failed. The student may already be enrolled in this course." });
        } finally {
            setSaving(false);
        }

    };

    return (

        <AdminLayout>

            <PageHeader
                title="Student Enrollment"
                subtitle="Allocate a student to a course."
            />

            <div className="row">
                <div className="col-lg-6">

                    <div className="ch-form-card mb-0">

                        <div className="d-flex align-items-center gap-2 mb-3">
                            <div className="ch-stat-card__icon" style={{ width: 36, height: 36 }}>
                                <ClipboardListIcon size={17} />
                            </div>
                            <div className="ch-form-card__title mb-0">Enroll a student</div>
                        </div>

                        {feedback && (
                            <div className={`alert alert-${feedback.type} mb-3`}>
                                {feedback.text}
                            </div>
                        )}

                        <div className="mb-3">
                            <label className="form-label ch-required">Student</label>

                            <select
                                className="form-select"
                                value={studentId}
                                onChange={(e) => setStudentId(e.target.value)}
                            >
                                <option value="">Select Student</option>

                                {students.map(student => (
                                    <option key={student.id} value={student.id}>
                                        {student.fullName} ({student.usn})
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label ch-required">Course</label>

                            <select
                                className="form-select"
                                value={courseId}
                                onChange={(e) => setCourseId(e.target.value)}
                            >
                                <option value="">Select Course</option>

                                {courses.map(course => (
                                    <option key={course.id} value={course.id}>
                                        {course.courseCode} - {course.courseName}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <button
                            className="btn btn-primary w-100"
                            onClick={handleEnrollment}
                            disabled={saving}
                        >
                            {saving ? "Enrolling..." : "Enroll Student"}
                        </button>

                    </div>

                </div>
            </div>

        </AdminLayout>

    );

}

export default StudentEnrollmentPage;
