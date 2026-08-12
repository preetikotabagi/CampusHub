import { useEffect, useState } from "react";
import FacultyLayout from "../../components/layout/FacultyLayout";
import { getMyCourses } from "../../services/courseService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { BookIcon } from "../../components/icons/Icon";

function MyCoursesPage() {

    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadCourses();
    }, []);

    const loadCourses = async () => {
        try {
            setLoading(true);
            const data = await getMyCourses();
            setCourses(data);
        } catch (error) {
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <FacultyLayout>

            <PageHeader
                title="My Courses"
                subtitle="Courses currently assigned to you."
            />

            {loading ? (
                <LoadingState label="Loading your courses..." />
            ) : courses.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={BookIcon}
                        title="No courses assigned"
                        text="You don't have any courses assigned yet. Check back once the admin allocates one."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Course Code</th>
                                    <th>Course Name</th>
                                    <th>Credits</th>
                                    <th>Department</th>
                                </tr>
                            </thead>

                            <tbody>
                                {courses.map(course => (
                                    <tr key={course.id}>
                                        <td className="ch-mono">{course.courseCode}</td>
                                        <td className="fw-semibold">{course.courseName}</td>
                                        <td>{course.credits}</td>
                                        <td>{course.departmentName}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

        </FacultyLayout>
    );
}

export default MyCoursesPage;
