import AdminLayout from "../../components/layout/AdminLayout";
import { useEffect, useState } from "react";
import { getAllDepartments } from "../../services/departmentService";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { UsersIcon, EditIcon, TrashIcon, PlusIcon } from "../../components/icons/Icon";

import {
    getAllFaculties,
    createFaculty,
    updateFaculty,
    deleteFaculty
} from "../../services/facultyService";

function FacultyPage() {

    const [faculties, setFaculties] = useState([]);
    const [departments, setDepartments] = useState([]);

    const [name, setName] = useState("");
    const [employeeId, setEmployeeId] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [designation, setDesignation] = useState("");
    const [departmentId, setDepartmentId] = useState("");

    const [editingId, setEditingId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    const loadFaculties = async () => {
        try {
            setLoading(true);
            const faculties = await getAllFaculties();
            setFaculties(faculties);
        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Failed to load faculty." });
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
        setEmployeeId("");
        setEmail("");
        setPhoneNumber("");
        setDesignation("");
        setDepartmentId("");
        setEditingId(null);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const faculty = {
            fullName: name,
            employeeId,
            email,
            phoneNumber,
            designation,
            departmentId: Number(departmentId),
        };

        setSaving(true);
        setFeedback(null);

        try {
            if (editingId) {
                await updateFaculty(editingId, faculty);
                setFeedback({ type: "success", text: "Faculty updated successfully." });
            } else {
                await createFaculty(faculty);
                setFeedback({ type: "success", text: "Faculty added successfully." });
            }

            resetForm();
            loadFaculties();
        } catch (error) {
            console.error(error);
            setFeedback({
                type: "danger",
                text: editingId ? "Failed to update faculty." : "Failed to add faculty.",
            });
        } finally {
            setSaving(false);
        }
    };

    useEffect(() => {
        loadFaculties();
        loadDepartments();
    }, []);

    return (

        <AdminLayout>

            <PageHeader
                title="Faculty Management"
                subtitle="Add faculty members and assign them to departments."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            <div className="ch-form-card">
                <div className="ch-form-card__title">
                    {editingId ? "Update faculty" : "Add a new faculty member"}
                </div>

                <div className="row g-3">

                    <div className="col-md-4">
                        <label className="form-label ch-required">Full name</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Faculty name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Employee ID</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Employee ID"
                            value={employeeId}
                            onChange={(e) => setEmployeeId(e.target.value)}
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
                        <label className="form-label">Phone number</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Phone number"
                            value={phoneNumber}
                            onChange={(e) => setPhoneNumber(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label">Designation</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="e.g. Assistant Professor"
                            value={designation}
                            onChange={(e) => setDesignation(e.target.value)}
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
                        {saving ? "Saving..." : editingId ? "Update Faculty" : "Add Faculty"}
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
                <LoadingState label="Loading faculty..." />
            ) : faculties.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={UsersIcon}
                        title="No faculty found"
                        text="Add your first faculty member using the form above."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Employee ID</th>
                                    <th>Email</th>
                                    <th>Designation</th>
                                    <th>Department</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {faculties.map((faculty) => (
                                    <tr key={faculty.id}>
                                        <td className="fw-semibold">{faculty.fullName}</td>
                                        <td className="ch-mono">{faculty.employeeId}</td>
                                        <td>{faculty.email}</td>
                                        <td>{faculty.designation}</td>
                                        <td>{faculty.departmentName}</td>
                                        <td>
                                            <div className="d-flex gap-2">
                                                <button
                                                    className="btn btn-secondary btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={() => {
                                                        setEditingId(faculty.id);
                                                        setName(faculty.fullName);
                                                        setEmployeeId(faculty.employeeId);
                                                        setEmail(faculty.email);
                                                        setDesignation(faculty.designation);
                                                        setDepartmentId(faculty.departmentId);
                                                    }}
                                                >
                                                    <EditIcon size={14} /> Edit
                                                </button>

                                                <button
                                                    className="btn btn-danger btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={async () => {
                                                        if (!window.confirm("Are you sure you want to delete this faculty?")) {
                                                            return;
                                                        }

                                                        try {
                                                            await deleteFaculty(faculty.id);
                                                            setFeedback({ type: "success", text: "Faculty deleted successfully." });
                                                            loadFaculties();
                                                        } catch (error) {
                                                            console.error(error);
                                                            setFeedback({ type: "danger", text: "Failed to delete faculty." });
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

export default FacultyPage;
