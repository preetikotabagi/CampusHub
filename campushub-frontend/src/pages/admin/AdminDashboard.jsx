import { useEffect, useState } from "react";
import AdminLayout from "../../components/layout/AdminLayout";
import { getDashboard } from "../../services/dashboardService";
import DashboardCard from "../../components/dashboard/DashboardCard";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import ErrorState from "../../components/common/ErrorState";

function AdminDashboard() {

    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadDashboard();
    }, []);

    const loadDashboard = async () => {
        try {
            setLoading(true);
            setError("");
            const data = await getDashboard();
            setDashboard(data);
        } catch (err) {
            console.error(err);
            setError("We couldn't load the dashboard right now.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <AdminLayout>

            <PageHeader
                title="Welcome back, Administrator"
                subtitle="Here's what's happening across your university today."
            />

            {loading && <LoadingState label="Loading dashboard..." />}

            {!loading && error && (
                <ErrorState message={error} onRetry={loadDashboard} />
            )}

            {!loading && !error && dashboard && (

                <div className="row g-3">

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Students"
                            value={dashboard.totalStudents}
                        />
                    </div>

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Faculty"
                            value={dashboard.totalFaculty}
                        />
                    </div>

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Departments"
                            value={dashboard.totalDepartments}
                        />
                    </div>

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Courses"
                            value={dashboard.totalCourses}
                        />
                    </div>

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Attendance Records"
                            value={dashboard.totalAttendanceRecords}
                        />
                    </div>

                    <div className="col-sm-6 col-xl-4">
                        <DashboardCard
                            title="Marks Records"
                            value={dashboard.totalMarksRecords}
                        />
                    </div>

                </div>
            )}

        </AdminLayout>
    );
}

export default AdminDashboard;
