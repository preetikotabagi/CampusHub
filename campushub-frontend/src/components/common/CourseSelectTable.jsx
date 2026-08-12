import LoadingState from "./LoadingState";
import EmptyState from "./EmptyState";
import { BookIcon } from "../icons/Icon";

/**
 * A reusable "pick a course to continue" table, used by both the
 * Admin and Faculty attendance/marks flows (select a course, then
 * drill into that course's attendance or marks screen).
 */
function CourseSelectTable({ courses, loading, onSelect, emptyText }) {

    if (loading) {
        return <LoadingState label="Loading courses..." />;
    }

    if (!courses || courses.length === 0) {
        return (
            <div className="ch-table-card">
                <EmptyState
                    icon={BookIcon}
                    title="No courses found"
                    text={emptyText || "There are no courses to show here yet."}
                />
            </div>
        );
    }

    return (
        <div className="ch-table-card">
            <div className="ch-table-scroll">
                <table className="table table-hover mb-0">
                    <thead>
                        <tr>
                            <th>Course Code</th>
                            <th>Course Name</th>
                            <th>Semester</th>
                            <th>Department</th>
                        </tr>
                    </thead>

                    <tbody>
                        {courses.map((course) => (
                            <tr
                                key={course.id}
                                style={{ cursor: "pointer" }}
                                onClick={() => onSelect(course)}
                            >
                                <td className="ch-mono">{course.courseCode}</td>
                                <td className="fw-semibold">{course.courseName}</td>
                                <td>{course.semester}</td>
                                <td>{course.departmentName}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default CourseSelectTable;
