import { useEffect, useState } from "react";
import StudentLayout from "../../components/layout/StudentLayout";
import { getMyMarks } from "../../services/studentService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import StatusBadge from "../../components/common/StatusBadge";
import { ClipboardListIcon } from "../../components/icons/Icon";

function StudentMarksPage() {

    const [marks, setMarks] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadMarks();
    }, []);

    const loadMarks = async () => {
        try {
            setLoading(true);
            const data = await getMyMarks();
            setMarks(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (

        <StudentLayout>

            <PageHeader
                title="My Marks"
                subtitle="IA, ESA and total marks for each of your courses."
            />

            {loading ? (
                <LoadingState label="Loading your marks..." />
            ) : marks.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={ClipboardListIcon}
                        title="No marks recorded yet"
                        text="Your marks will appear here once your instructors enter them."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover mb-0">

                            <thead>

                                <tr>

                                    <th>Course</th>
                                    <th>IA1</th>
                                    <th>IA2</th>
                                    <th>ESA</th>
                                    <th>Total</th>
                                    <th>Grade</th>

                                </tr>

                            </thead>

                            <tbody>

                                {marks.map(mark => (

                                    <tr key={mark.id}>

                                        <td className="fw-semibold">{mark.courseName}</td>
                                        <td>{mark.ia1Marks}</td>
                                        <td>{mark.ia2Marks}</td>
                                        <td>{mark.esaMarks}</td>
                                        <td className="fw-semibold">{mark.totalMarks}</td>
                                        <td>
                                            <StatusBadge status={mark.grade} />
                                        </td>

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

export default StudentMarksPage;
