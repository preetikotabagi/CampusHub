import FacultyLayout from "../../components/layout/FacultyLayout";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getStudentsByCourse } from "../../services/courseService";
import {
    saveMarks,
    getMarksByCourse
} from "../../services/marksService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { UsersIcon, SaveIcon } from "../../components/icons/Icon";

function FacultyMarksPage() {

    const { courseId } = useParams();
    const [students, setStudents] = useState([]);
    const [marks, setMarks] = useState({});
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    const loadStudents = async () => {
        try {

            const data = await getStudentsByCourse(courseId);
            setStudents(data);

        } catch (error) {

            console.error(error);

        }
    };

    const loadMarks = async () => {

        try {

            const data = await getMarksByCourse(courseId);

            const marksMap = {};

            data.forEach(mark => {

                marksMap[mark.studentId] = {

                    ia1Marks: mark.ia1Marks,
                    ia2Marks: mark.ia2Marks,
                    esaMarks: mark.esaMarks

                };

            });

            setMarks(marksMap);

        } catch (error) {

            console.error(error);

        }

    };

    const handleSaveMarks = async () => {

        setSaving(true);
        setFeedback(null);

        try {

            const marksList = students.map(student => ({

                studentId: student.id,
                courseId: Number(courseId),
                ia1Marks: marks[student.id]?.ia1Marks || 0,
                ia2Marks: marks[student.id]?.ia2Marks || 0,
                esaMarks: marks[student.id]?.esaMarks || 0

            }));

            await saveMarks(marksList);

            setFeedback({ type: "success", text: "Marks saved successfully." });

        } catch (error) {

            console.error(error);
            setFeedback({ type: "danger", text: "Failed to save marks." });

        } finally {
            setSaving(false);
        }

    };

    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            await loadStudents();
            await loadMarks();
            setLoading(false);
        };
        loadData();
    }, []);

    const computeGrade = (total) => {
        if (total >= 90) return "S";
        if (total >= 80) return "A";
        if (total >= 70) return "B";
        if (total >= 60) return "C";
        if (total >= 55) return "D";
        if (total >= 50) return "E";
        if (total >= 40) return "F";
        return "X";
    };

    return (

        <FacultyLayout>

            <PageHeader
                title="Marks Management"
                subtitle="Enter IA1, IA2 and ESA marks. Total and grade are calculated live."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            {loading ? (
                <LoadingState label="Loading students and marks..." />
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
                                        <th>IA1 (25)</th>
                                        <th>IA2 (25)</th>
                                        <th>ESA (50)</th>
                                        <th>Total</th>
                                        <th>Grade</th>
                                    </tr>

                                </thead>

                                <tbody>

                                    {students.map(student => (

                                        <tr key={student.id}>
                                            <td className="ch-mono">{student.usn}</td>
                                            <td className="fw-semibold">{student.fullName}</td>

                                            <td>
                                                <input
                                                    type="number"
                                                    className="form-control"
                                                    style={{ maxWidth: 90 }}
                                                    min="0"
                                                    max="25"
                                                    value={marks[student.id]?.ia1Marks || ""}
                                                    onChange={(e) =>
                                                        setMarks(prev => ({
                                                            ...prev,
                                                            [student.id]: {
                                                                ...prev[student.id],
                                                                ia1Marks: Number(e.target.value),
                                                                ia2Marks: prev[student.id]?.ia2Marks || 0,
                                                                esaMarks: prev[student.id]?.esaMarks || 0
                                                            }
                                                        }))
                                                    }
                                                />
                                            </td>

                                            <td>
                                                <input
                                                    type="number"
                                                    className="form-control"
                                                    style={{ maxWidth: 90 }}
                                                    min="0"
                                                    max="25"
                                                    value={marks[student.id]?.ia2Marks || ""}
                                                    onChange={(e) =>
                                                        setMarks(prev => ({
                                                            ...prev,
                                                            [student.id]: {
                                                                ...prev[student.id],
                                                                ia1Marks: prev[student.id]?.ia1Marks || 0,
                                                                ia2Marks: Number(e.target.value),
                                                                esaMarks: prev[student.id]?.esaMarks || 0
                                                            }
                                                        }))
                                                    }
                                                />
                                            </td>

                                            <td>
                                                <input
                                                    type="number"
                                                    className="form-control"
                                                    style={{ maxWidth: 90 }}
                                                    min="0"
                                                    max="50"
                                                    value={marks[student.id]?.esaMarks || ""}
                                                    onChange={(e) =>
                                                        setMarks(prev => ({
                                                            ...prev,
                                                            [student.id]: {
                                                                ...prev[student.id],
                                                                ia1Marks: prev[student.id]?.ia1Marks || 0,
                                                                ia2Marks: prev[student.id]?.ia2Marks || 0,
                                                                esaMarks: Number(e.target.value)
                                                            }
                                                        }))
                                                    }
                                                />
                                            </td>

                                            <td className="fw-semibold">
                                                {marks[student.id]
                                                    ? (marks[student.id].ia1Marks || 0) +
                                                    (marks[student.id].ia2Marks || 0) +
                                                    (marks[student.id].esaMarks || 0)
                                                    : "-"}
                                            </td>

                                            <td>
                                                {marks[student.id] ? (
                                                    <StatusBadge
                                                        status={computeGrade(
                                                            (marks[student.id].ia1Marks || 0) +
                                                            (marks[student.id].ia2Marks || 0) +
                                                            (marks[student.id].esaMarks || 0)
                                                        )}
                                                    />
                                                ) : (
                                                    "-"
                                                )}
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
                            onClick={handleSaveMarks}
                            disabled={saving}
                        >
                            <SaveIcon size={15} />
                            {saving ? "Saving..." : "Save Marks"}
                        </button>
                    </div>
                </>
            )}

        </FacultyLayout>

    );
}

export default FacultyMarksPage;
