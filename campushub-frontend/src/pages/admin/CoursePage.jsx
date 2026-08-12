import AdminLayout from "../../components/layout/AdminLayout";
import { useEffect, useState } from "react";

import { getAllDepartments } from "../../services/departmentService";
import { getAllFaculties } from "../../services/facultyService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { BookIcon, EditIcon, TrashIcon, PlusIcon } from "../../components/icons/Icon";

import {
    getAllCourses,
    createCourse,
    updateCourse,
    deleteCourse
} from "../../services/courseService";

function CoursePage() {

    const [courses, setCourses] = useState([]);

    const [departments, setDepartments] = useState([]);
    const [faculties, setFaculties] = useState([]);

    const [courseCode, setCourseCode] = useState("");
    const [courseName, setCourseName] = useState("");
    const [credits, setCredits] = useState("");
    const [semester, setSemester] = useState("");
    const [academicYear, setAcademicYear] = useState("");
    const [departmentId, setDepartmentId] = useState("");
    const [facultyId, setFacultyId] = useState("");

    const [editingId, setEditingId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    const loadCourses = async () => {
        try {
            setLoading(true);
            const courses = await getAllCourses();
            setCourses(courses);
        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Failed to load courses." });
        } finally {
            setLoading(false);
        }
    };

    const loadDepartments = async () => {
        try {
            const departments = await getAllDepartments();
            setDepartments(departments);
        } catch (error) {
            console.error(error);
        }
    };

    const loadFaculties = async () => {
        try {
            const faculties = await getAllFaculties();
            setFaculties(faculties);
        } catch (error) {
            console.error(error);
        }
    };

    const resetForm = () => {
        setCourseCode("");
        setCourseName("");
        setCredits("");
        setSemester("");
        setAcademicYear("");
        setDepartmentId("");
        setFacultyId("");
        setEditingId(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const course = {
            courseCode,
            courseName,
            credits: Number(credits),
            semester: Number(semester),
            academicYear,
            departmentId: Number(departmentId),
            facultyId: Number(facultyId),
        };

        setSaving(true);
        setFeedback(null);

        try {
            if (editingId) {
                await updateCourse(editingId, course);
                setFeedback({ type: "success", text: "Course updated successfully." });
            } else {
                await createCourse(course);
                setFeedback({ type: "success", text: "Course added successfully." });
            }

            resetForm();
            loadCourses();

        } catch (error) {
            console.error(error);
            setFeedback({
                type: "danger",
                text: editingId ? "Failed to update course." : "Failed to add course.",
            });
        } finally {
            setSaving(false);
        }
    };

    useEffect(() => {
        loadCourses();
        loadDepartments();
        loadFaculties();
    }, []);

    return (
        <AdminLayout>

            <PageHeader
                title="Course Management"
                subtitle="Define courses and assign a faculty member to teach each one."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            <div className="ch-form-card">
                <div className="ch-form-card__title">
                    {editingId ? "Update course" : "Add a new course"}
                </div>

                <div className="row g-3">

                    <div className="col-md-3">
                        <label className="form-label ch-required">Course code</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="e.g. CS301"
                            value={courseCode}
                            onChange={(e) => setCourseCode(e.target.value)}
                        />
                    </div>

                    <div className="col-md-5">
                        <label className="form-label ch-required">Course name</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Course name"
                            value={courseName}
                            onChange={(e) => setCourseName(e.target.value)}
                        />
                    </div>

                    <div className="col-md-2">
                        <label className="form-label ch-required">Credits</label>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Credits"
                            value={credits}
                            onChange={(e) => setCredits(e.target.value)}
                        />
                    </div>

                    <div className="col-md-2">
                        <label className="form-label ch-required">Semester</label>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Semester"
                            value={semester}
                            onChange={(e) => setSemester(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Academic year</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Ex: 2026-27"
                            value={academicYear}
                            onChange={(e) => setAcademicYear(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Department</label>
                        <select
                            className="form-select"
                            value={departmentId}
                            onChange={(e) => setDepartmentId(e.target.value)}
                        >
                            <option value="">Select Department</option>
                            {departments.map((department) => (
                                <option key={department.id} value={department.id}>
                                    {department.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Faculty</label>
                        <select
                            className="form-select"
                            value={facultyId}
                            onChange={(e) => setFacultyId(e.target.value)}
                        >
                            <option value="">Select Faculty</option>
                            {faculties.map((faculty) => (
                                <option key={faculty.id} value={faculty.id}>
                                    {faculty.fullName}
                                </option>
                            ))}
                        </select>
                    </div>

                </div>

                <div className="d-flex gap-2 mt-3">
                    <button
                        className="btn btn-primary d-inline-flex align-items-center gap-2"
                        type="button"
                        onClick={handleSubmit}
                        disabled={saving}
                    >
                        {!editingId && <PlusIcon size={15} />}
                        {saving ? "Saving..." : editingId ? "Update Course" : "Add Course"}
                    </button>

                    {editingId && (
                        <button
                            className="btn btn-secondary"
                            type="button"
                            onClick={resetForm}
                        >
                            Cancel
                        </button>
                    )}
                </div>

            </div>

            {loading ? (
                <LoadingState label="Loading courses..." />
            ) : courses.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={BookIcon}
                        title="No courses found"
                        text="Add your first course using the form above."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover">
                            <thead>
                                <tr>
                                    <th>Course Code</th>
                                    <th>Course Name</th>
                                    <th>Credits</th>
                                    <th>Semester</th>
                                    <th>Academic Year</th>
                                    <th>Department</th>
                                    <th>Faculty</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {courses.map((course) => (
                                    <tr key={course.id}>
                                        <td className="ch-mono">{course.courseCode}</td>
                                        <td className="fw-semibold">{course.courseName}</td>
                                        <td>{course.credits}</td>
                                        <td>{course.semester}</td>
                                        <td>{course.academicYear}</td>
                                        <td>{course.departmentName}</td>
                                        <td>{course.facultyName}</td>

                                        <td>
                                            <div className="d-flex gap-2">
                                                <button
                                                    className="btn btn-secondary btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={() => {
                                                        setEditingId(course.id);
                                                        setCourseCode(course.courseCode);
                                                        setCourseName(course.courseName);
                                                        setCredits(course.credits);
                                                        setSemester(course.semester);
                                                        setAcademicYear(course.academicYear);
                                                        setDepartmentId(course.departmentId);
                                                        setFacultyId(course.facultyId);
                                                    }}
                                                >
                                                    <EditIcon size={14} /> Edit
                                                </button>

                                                <button
                                                    className="btn btn-danger btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={async () => {

                                                        if (!window.confirm("Are you sure you want to delete this course?")) {
                                                            return;
                                                        }

                                                        try {
                                                            await deleteCourse(course.id);
                                                            setFeedback({ type: "success", text: "Course deleted successfully." });
                                                            loadCourses();
                                                        } catch (error) {
                                                            console.error(error);
                                                            setFeedback({ type: "danger", text: "Failed to delete course." });
                                                        }

                                                    }}
                                                >
                                                    <TrashIcon size={14} /> Delete
                                                </button>
                                            </div>
                                        </td>
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

export default CoursePage;
