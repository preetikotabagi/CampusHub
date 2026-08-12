import { useEffect, useState } from "react";
import StudentLayout from "../../components/layout/StudentLayout";
import { getMyProfile, getMyAttendance, getMyGpa } from "../../services/studentService";
import { getMyCourses } from "../../services/studentCourseService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import ErrorState from "../../components/common/ErrorState";
import {
    BookIcon,
    CalendarIcon,
    AwardIcon,
    IdCardIcon,
    BuildingIcon,
} from "../../components/icons/Icon";

function StudentDashboard() {

    const [profile, setProfile] = useState(null);
    const [courses, setCourses] = useState([]);
    const [attendance, setAttendance] = useState([]);
    const [gpa, setGpa] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            setLoading(true);
            setError("");

            const profileData = await getMyProfile();
            const courseData = await getMyCourses();
            const attendanceData = await getMyAttendance();

            setProfile(profileData);
            setCourses(courseData);
            setAttendance(attendanceData);

            // GPA is optional/secondary — a failure here shouldn't block
            // the rest of the dashboard from rendering.
            try {
                const gpaData = await getMyGpa();
                setGpa(gpaData);
            } catch (gpaErr) {
                console.error(gpaErr);
            }

        } catch (err) {
            console.error(err);
            setError("We couldn't load your dashboard right now.");
        } finally {
            setLoading(false);
        }
    };

    const attendancePercentage =
        attendance.length === 0
            ? null
            : Math.round(
                  (attendance.filter((a) => a.status === "PRESENT").length /
                      attendance.length) *
                      100
              );

    return (
        <StudentLayout>

            <PageHeader
                title={`Welcome, ${profile ? profile.fullName.split(" ")[0] : "Student"}`}
                subtitle="Your academic overview at a glance."
            />

            {loading && <LoadingState label="Loading your dashboard..." />}

            {!loading && error && (
                <ErrorState message={error} onRetry={loadData} />
            )}

            {!loading && !error && profile && (
                <>
                    <div className="card mb-4">
                        <div className="card-body">
                            <div className="d-flex align-items-center gap-3 mb-3">
                                <div className="ch-avatar-ring">
                                    {profile.fullName?.charAt(0)}
                                </div>
                                <div>
                                    <h4 className="mb-0">{profile.fullName}</h4>
                                    <div className="ch-muted" style={{ fontSize: 13 }}>
                                        {profile.usn}
                                    </div>
                                </div>
                            </div>

                            <div className="row g-3">
                                <div className="col-sm-6 col-lg-4 d-flex align-items-center gap-2">
                                    <IdCardIcon size={16} className="ch-muted" />
                                    <span className="ch-muted" style={{ fontSize: 13.5 }}>
                                        {profile.email}
                                    </span>
                                </div>
                                <div className="col-sm-6 col-lg-4 d-flex align-items-center gap-2">
                                    <BuildingIcon size={16} className="ch-muted" />
                                    <span className="ch-muted" style={{ fontSize: 13.5 }}>
                                        {profile.departmentName}
                                    </span>
                                </div>
                                <div className="col-sm-6 col-lg-4 d-flex align-items-center gap-2">
                                    <BookIcon size={16} className="ch-muted" />
                                    <span className="ch-muted" style={{ fontSize: 13.5 }}>
                                        Semester {profile.semester}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="row g-3">

                        <div className="col-sm-6 col-xl-3">
                            <div className="ch-stat-card">
                                <div>
                                    <p className="ch-stat-card__label">Enrolled Courses</p>
                                    <h2 className="ch-stat-card__value">{courses.length}</h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <BookIcon size={20} />
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <div className="ch-stat-card">
                                <div>
                                    <p className="ch-stat-card__label">Attendance</p>
                                    <h2 className="ch-stat-card__value">
                                        {attendancePercentage === null
                                            ? "N/A"
                                            : `${attendancePercentage}%`}
                                    </h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <CalendarIcon size={20} />
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <div className="ch-stat-card">
                                <div>
                                    <p className="ch-stat-card__label">CGPA</p>
                                    <h2 className="ch-stat-card__value">
                                        {gpa?.cgpa != null ? gpa.cgpa.toFixed(2) : "N/A"}
                                    </h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <AwardIcon size={20} />
                                </div>
                            </div>
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <div className="ch-stat-card">
                                <div>
                                    <p className="ch-stat-card__label">Credits Completed</p>
                                    <h2 className="ch-stat-card__value">
                                        {gpa?.totalCredits ?? "N/A"}
                                    </h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <IdCardIcon size={20} />
                                </div>
                            </div>
                        </div>

                    </div>
                </>
            )}

        </StudentLayout>
    );
}

export default StudentDashboard;
