import { useEffect, useState } from "react";
import StudentLayout from "../../components/layout/StudentLayout";
import { getMyGpa, getMyMarks } from "../../services/studentService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import ErrorState from "../../components/common/ErrorState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { AwardIcon, TrendUpIcon, IdCardIcon } from "../../components/icons/Icon";

function StudentResultsPage() {

    const [gpaData, setGpaData] = useState(null);
    const [marks, setMarks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadResults();
    }, []);

    const loadResults = async () => {

        try {
            setLoading(true);
            setError("");

            const data = await getMyGpa();
            setGpaData(data);

            try {
                const marksData = await getMyMarks();
                setMarks(marksData);
            } catch (marksErr) {
                console.error(marksErr);
            }

        } catch (err) {
            setError(
                err.response?.data?.message ||
                "Unable to load your results right now."
            );
        } finally {
            setLoading(false);
        }
    };

    const bestSgpa =
        gpaData?.semesterGpas && gpaData.semesterGpas.length > 0
            ? Math.max(...gpaData.semesterGpas.map((s) => s.sgpa || 0))
            : null;

    return (

        <StudentLayout>

            <PageHeader
                title="My Results"
                subtitle="Your cumulative and semester-wise academic performance."
            />

            {loading && <LoadingState label="Loading your results..." />}

            {!loading && error && (
                <ErrorState message={error} onRetry={loadResults} />
            )}

            {!loading && !error && gpaData && (

                <>
                    {/* ---------- Summary cards ---------- */}
                    <div className="row g-3 mb-4">

                        <div className="col-md-4">
                            <div
                                className="card h-100"
                                style={{
                                    background:
                                        "linear-gradient(150deg, var(--ch-navy-900) 0%, var(--ch-navy-950) 100%)",
                                    border: "none",
                                    color: "#fff",
                                }}
                            >
                                <div className="card-body">
                                    <div className="d-flex align-items-center justify-content-between mb-2">
                                        <span
                                            style={{
                                                fontSize: 12,
                                                fontWeight: 700,
                                                letterSpacing: "0.06em",
                                                textTransform: "uppercase",
                                                color: "#a9b2c8",
                                            }}
                                        >
                                            Overall CGPA
                                        </span>
                                        <AwardIcon size={18} style={{ color: "var(--ch-accent)" }} />
                                    </div>

                                    <div
                                        style={{
                                            fontFamily: "var(--ch-font-display)",
                                            fontSize: 40,
                                            fontWeight: 700,
                                        }}
                                    >
                                        {gpaData.cgpa != null ? gpaData.cgpa.toFixed(2) : "N/A"}
                                    </div>

                                    <div className="ch-divider-gold" />

                                    <div style={{ fontSize: 12.5, color: "#a9b2c8" }}>
                                        Based on {gpaData.totalCredits ?? 0} completed credits
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4">
                            <div className="ch-stat-card h-100">
                                <div>
                                    <p className="ch-stat-card__label">Best Semester SGPA</p>
                                    <h2 className="ch-stat-card__value">
                                        {bestSgpa != null ? bestSgpa.toFixed(2) : "N/A"}
                                    </h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <TrendUpIcon size={20} />
                                </div>
                            </div>
                        </div>

                        <div className="col-md-4">
                            <div className="ch-stat-card h-100">
                                <div>
                                    <p className="ch-stat-card__label">Semesters Recorded</p>
                                    <h2 className="ch-stat-card__value">
                                        {gpaData.semesterGpas?.length ?? 0}
                                    </h2>
                                </div>
                                <div className="ch-stat-card__icon">
                                    <IdCardIcon size={20} />
                                </div>
                            </div>
                        </div>

                    </div>

                    {/* ---------- Semester-wise SGPA ---------- */}
                    <div className="ch-section-title">Semester-wise SGPA</div>

                    {(!gpaData.semesterGpas || gpaData.semesterGpas.length === 0) ? (
                        <div className="ch-table-card mb-4">
                            <EmptyState
                                title="No semester results yet"
                                text="Your semester-wise SGPA will appear here once marks have been recorded for at least one course."
                            />
                        </div>
                    ) : (
                        <div className="ch-table-card mb-4">
                            <div className="ch-table-scroll">
                                <table className="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Semester</th>
                                            <th>SGPA</th>
                                            <th>Credits</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {gpaData.semesterGpas.map((sem) => (
                                            <tr key={sem.semester}>
                                                <td className="fw-semibold">
                                                    Semester {sem.semester}
                                                </td>
                                                <td>{sem.sgpa?.toFixed(2)}</td>
                                                <td>{sem.totalCredits}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}

                    {/* ---------- Course-wise performance ---------- */}
                    <div className="ch-section-title">Course-wise Performance</div>

                    {marks.length === 0 ? (
                        <div className="ch-table-card">
                            <EmptyState
                                title="No course marks recorded yet"
                                text="Once your instructors enter IA/ESA marks, your course-wise grades will show up here."
                            />
                        </div>
                    ) : (
                        <div className="ch-table-card">
                            <div className="ch-table-scroll">
                                <table className="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Course</th>
                                            <th>Total Marks</th>
                                            <th>Grade</th>
                                            <th>Grade Point</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {marks.map((mark) => (
                                            <tr key={mark.id}>
                                                <td className="fw-semibold">{mark.courseName}</td>
                                                <td>{mark.totalMarks}</td>
                                                <td>
                                                    <StatusBadge status={mark.grade} />
                                                </td>
                                                <td>{mark.gradePoint}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}

                </>
            )}

        </StudentLayout>

    );
}

export default StudentResultsPage;
