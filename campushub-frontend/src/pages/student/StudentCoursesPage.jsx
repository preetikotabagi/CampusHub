import { useEffect, useState } from "react";
import StudentLayout from "../../components/layout/StudentLayout";
import { getMyCourses } from "../../services/studentCourseService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { BookIcon } from "../../components/icons/Icon";

function StudentCoursesPage() {

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
        <StudentLayout>

            <PageHeader
                title="My Courses"
                subtitle="Courses you're currently enrolled in."
            />

            {loading ? (
                <LoadingState label="Loading your courses..." />
            ) : courses.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={BookIcon}
                        title="No courses yet"
                        text="You haven't been enrolled in any courses yet."
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
                                    <th>Faculty</th>
                                    <th>Credits</th>

                                </tr>

                            </thead>

                            <tbody>

                                {courses.map(course => (

                                    <tr key={course.id}>

                                        <td className="ch-mono">{course.courseCode}</td>
                                        <td className="fw-semibold">{course.courseName}</td>
                                        <td>{course.facultyName}</td>
                                        <td>{course.credits}</td>

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

export default StudentCoursesPage;
