import AdminLayout from "../../components/layout/AdminLayout";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getMarksByCourse } from "../../services/marksService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { AwardIcon } from "../../components/icons/Icon";

function MarksPage() {

    const { courseId } = useParams();

    const [marksList, setMarksList] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadMarks = async () => {

        try {

            const marks = await getMarksByCourse(courseId);

            setMarksList(marks);

        } catch (error) {

            console.error(error);

        }

    };

    useEffect(() => {
        const load = async () => {
            setLoading(true);
            await loadMarks();
            setLoading(false);
        };
        load();
    }, []);

    return (

        <AdminLayout>

            <PageHeader
                title="Student Marks Records"
                subtitle="IA, ESA, total and grade for every student in this course."
            />

            {loading ? (
                <LoadingState label="Loading marks records..." />
            ) : marksList.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={AwardIcon}
                        title="No marks records found"
                        text="Marks haven't been entered for this course yet."
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
                                    <th>IA1 (25)</th>
                                    <th>IA2 (25)</th>
                                    <th>ESA (50)</th>
                                    <th>Total</th>
                                    <th>Grade</th>
                                    <th>Grade Point</th>
                                </tr>

                            </thead>

                            <tbody>

                                {marksList.map((mark) => (

                                    <tr key={mark.id}>

                                        <td className="fw-semibold">{mark.studentName}</td>
                                        <td>{mark.courseName}</td>
                                        <td>{mark.ia1Marks}</td>
                                        <td>{mark.ia2Marks}</td>
                                        <td>{mark.esaMarks}</td>
                                        <td className="fw-semibold">{mark.totalMarks}</td>
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

        </AdminLayout>

    );
}

export default MarksPage;
