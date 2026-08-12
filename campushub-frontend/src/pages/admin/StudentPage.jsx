import AdminLayout from "../../components/layout/AdminLayout";
import { useEffect, useState } from "react";
import { getAllDepartments } from "../../services/departmentService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { GraduationCapIcon, EditIcon, TrashIcon, PlusIcon } from "../../components/icons/Icon";

import {
    getAllStudents,
    createStudent,
    updateStudent,
    deleteStudent
} from "../../services/studentService";

function StudentPage() {

    const [students, setStudents] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [name, setName] = useState("");
    const [usn, setUsn] = useState("");
    const [email, setEmail] = useState("");
    const [semester, setSemester] = useState("");
    const [departmentId, setDepartmentId] = useState("");

    const [editingId, setEditingId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    const loadStudents = async () => {
        try {
            setLoading(true);
            const response = await getAllStudents();
            setStudents(response.data.data);
        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Failed to load students." });
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

    const resetForm = () => {
        setName("");
        setUsn("");
        setEmail("");
        setSemester("");
        setDepartmentId("");
        setEditingId(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const student = {
            fullName: name,
            usn,
            email,
            semester: Number(semester),
            departmentId: Number(departmentId),
        };

        setSaving(true);
        setFeedback(null);

        try {
            if (editingId) {
                await updateStudent(editingId, student);
                setFeedback({ type: "success", text: "Student updated successfully." });
            } else {
                await createStudent(student);
                setFeedback({ type: "success", text: "Student added successfully." });
            }

            resetForm();
            loadStudents();
        } catch (error) {
            console.error(error);
            setFeedback({
                type: "danger",
                text: editingId ? "Failed to update student." : "Failed to add student.",
            });
        } finally {
            setSaving(false);
        }
    };

    useEffect(() => {
        loadStudents();
        loadDepartments();
    }, []);

    return (

        <AdminLayout>

            <PageHeader
                title="Student Management"
                subtitle="Add students and keep their department and semester up to date."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            <div className="ch-form-card">
                <div className="ch-form-card__title">
                    {editingId ? "Update student" : "Add a new student"}
                </div>

                <div className="row g-3">

                    <div className="col-md-4">
                        <label className="form-label ch-required">Full name</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Student name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">USN</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="USN"
                            value={usn}
                            onChange={(e) => setUsn(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Email</label>
                        <input
                            type="email"
                            className="form-control"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
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

                </div>

                <div className="d-flex gap-2 mt-3">
                    <button
                        type="button"
                        className="btn btn-primary d-inline-flex align-items-center gap-2"
                        onClick={handleSubmit}
                        disabled={saving}
                    >
                        {!editingId && <PlusIcon size={15} />}
                        {saving ? "Saving..." : editingId ? "Update Student" : "Add Student"}
                    </button>

                    {editingId && (
                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={resetForm}
                        >
                            Cancel
                        </button>
                    )}
                </div>

            </div>

            {loading ? (
                <LoadingState label="Loading students..." />
            ) : students.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={GraduationCapIcon}
                        title="No students found"
                        text="Add your first student using the form above."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>USN</th>
                                    <th>Email</th>
                                    <th>Semester</th>
                                    <th>Department</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {students.map((student) => (
                                    <tr key={student.id}>
                                        <td className="fw-semibold">{student.fullName}</td>
                                        <td className="ch-mono">{student.usn}</td>
                                        <td>{student.email}</td>
                                        <td>{student.semester}</td>
                                        <td>{student.departmentName}</td>

                                        <td>
                                            <div className="d-flex gap-2">
                                                <button
                                                    className="btn btn-secondary btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={() => {
                                                        setEditingId(student.id);
                                                        setName(student.fullName);
                                                        setUsn(student.usn);
                                                        setEmail(student.email);
                                                        setSemester(student.semester);
                                                        setDepartmentId(student.departmentId);
                                                    }}
                                                >
                                                    <EditIcon size={14} /> Edit
                                                </button>

                                                <button
                                                    className="btn btn-danger btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={async () => {
                                                        if (!window.confirm("Are you sure you want to delete this student?")) {
                                                            return;
                                                        }

                                                        try {
                                                            await deleteStudent(student.id);
                                                            setFeedback({ type: "success", text: "Student deleted successfully." });
                                                            loadStudents();
                                                        } catch (error) {
                                                            console.error(error);
                                                            setFeedback({ type: "danger", text: "Failed to delete student." });
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

export default StudentPage;
