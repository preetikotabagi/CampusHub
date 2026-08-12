import { useEffect, useState } from "react";
import {
    getAllDepartments,
    createDepartment,
    updateDepartment,
    deleteDepartment
} from "../../services/departmentService";
import AdminLayout from "../../components/layout/AdminLayout";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import EmptyState from "../../components/common/EmptyState";
import { BuildingIcon, EditIcon, TrashIcon, PlusIcon } from "../../components/icons/Icon";

function DepartmentPage() {

    const [departments, setDepartments] = useState([]);
    const [name, setName] = useState("");
    const [code, setCode] = useState("");
    const [editingId, setEditingId] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [feedback, setFeedback] = useState(null);

    useEffect(() => {
        loadDepartments();
    }, []);

    const loadDepartments = async () => {
        try {
            setLoading(true);
            const data = await getAllDepartments();
            setDepartments(data);
        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Failed to load departments." });
        } finally {
            setLoading(false);
        }
    };

    const handleSaveDepartment = async () => {

        if (!name.trim() || !code.trim()) {
            setFeedback({ type: "danger", text: "Please enter both a name and a code." });
            return;
        }

        setSaving(true);
        setFeedback(null);

        try {

            if (editingId) {
                await updateDepartment(editingId, { name, code });
                setFeedback({ type: "success", text: "Department updated successfully." });
            } else {
                await createDepartment({ name, code });
                setFeedback({ type: "success", text: "Department added successfully." });
            }

            setEditingId(null);
            setName("");
            setCode("");

            loadDepartments();

        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Something went wrong. Please try again." });
        } finally {
            setSaving(false);
        }

    };

    const handleEdit = (department) => {
        setEditingId(department.id);
        setName(department.name);
        setCode(department.code);
    };

    const handleCancelEdit = () => {
        setEditingId(null);
        setName("");
        setCode("");
    };

    const handleDelete = async (id) => {

        if (!window.confirm("Delete this department?")) return;

        try {
            await deleteDepartment(id);
            setFeedback({ type: "success", text: "Department deleted." });
            loadDepartments();
        } catch (error) {
            console.error(error);
            setFeedback({ type: "danger", text: "Failed to delete department." });
        }

    };

    return (
        <AdminLayout>

            <PageHeader
                title="Departments"
                subtitle="Create and manage the academic departments in your university."
            />

            {feedback && (
                <div className={`alert alert-${feedback.type} mb-4`}>
                    {feedback.text}
                </div>
            )}

            <div className="ch-form-card">
                <div className="ch-form-card__title">
                    {editingId ? "Update department" : "Add a new department"}
                </div>

                <div className="row g-3 align-items-end">

                    <div className="col-md-5">
                        <label className="form-label ch-required">Department name</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="e.g. Computer Science"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                    </div>

                    <div className="col-md-4">
                        <label className="form-label ch-required">Department code</label>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="e.g. CSE"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                        />
                    </div>

                    <div className="col-md-3">
                        <div className="d-flex gap-2">
                            <button
                                className="btn btn-primary flex-grow-1 d-inline-flex align-items-center justify-content-center gap-2"
                                onClick={handleSaveDepartment}
                                disabled={saving}
                            >
                                {!editingId && <PlusIcon size={15} />}
                                {saving ? "Saving..." : editingId ? "Update" : "Add"}
                            </button>

                            {editingId && (
                                <button
                                    className="btn btn-secondary"
                                    onClick={handleCancelEdit}
                                >
                                    Cancel
                                </button>
                            )}
                        </div>
                    </div>

                </div>
            </div>

            {loading ? (
                <LoadingState label="Loading departments..." />
            ) : departments.length === 0 ? (
                <div className="ch-table-card">
                    <EmptyState
                        icon={BuildingIcon}
                        title="No departments found"
                        text="Add your first department using the form above."
                    />
                </div>
            ) : (
                <div className="ch-table-card">
                    <div className="ch-table-scroll">
                        <table className="table table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Code</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>

                            <tbody>
                                {departments.map((department) => (
                                    <tr key={department.id}>
                                        <td className="ch-mono ch-muted">{department.id}</td>
                                        <td className="fw-semibold">{department.name}</td>
                                        <td className="ch-mono">{department.code}</td>
                                        <td>
                                            <div className="d-flex gap-2">
                                                <button
                                                    className="btn btn-secondary btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={() => handleEdit(department)}
                                                >
                                                    <EditIcon size={14} /> Edit
                                                </button>

                                                <button
                                                    className="btn btn-danger btn-sm d-inline-flex align-items-center gap-1"
                                                    onClick={() => handleDelete(department.id)}
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

export default DepartmentPage;
