import FacultyLayout from "../../components/layout/FacultyLayout";
import DashboardCard from "../../components/dashboard/DashboardCard";
import PageHeader from "../../components/common/PageHeader";
import LoadingState from "../../components/common/LoadingState";
import ErrorState from "../../components/common/ErrorState";
import { useEffect, useState } from "react";
import { getFacultyDashboard } from "../../services/facultyService";
import { IdCardIcon, BuildingIcon, MailIcon, AwardIcon } from "../../components/icons/Icon";

function FacultyDashboard() {

    const [faculty, setFaculty] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        loadProfile();

    }, []);

    const loadProfile = async () => {

        try {

            setLoading(true);
            setError("");

            const data = await getFacultyDashboard();

            setFaculty(data);

        } catch (err) {

            console.error(err);
            setError("We couldn't load your dashboard right now.");

        } finally {
            setLoading(false);
        }

    };

    return (

        <FacultyLayout>

            <PageHeader
                title={`Welcome, ${faculty ? faculty.fullName : "Faculty"}`}
                subtitle="Your assigned courses and academic activity at a glance."
            />

            {loading && <LoadingState label="Loading your dashboard..." />}

            {!loading && error && (
                <ErrorState message={error} onRetry={loadProfile} />
            )}

            {!loading && !error && faculty && (
                <>
                    <div className="card mb-4">

                        <div className="card-body">

                            <div className="row g-3">

                                <div className="col-md-6 col-lg-3 d-flex align-items-center gap-3">
                                    <div className="ch-stat-card__icon">
                                        <IdCardIcon size={18} />
                                    </div>
                                    <div>
                                        <div className="ch-stat-card__label mb-1">Employee ID</div>
                                        <div className="fw-semibold">{faculty.employeeId}</div>
                                    </div>
                                </div>

                                <div className="col-md-6 col-lg-3 d-flex align-items-center gap-3">
                                    <div className="ch-stat-card__icon">
                                        <BuildingIcon size={18} />
                                    </div>
                                    <div>
                                        <div className="ch-stat-card__label mb-1">Department</div>
                                        <div className="fw-semibold">{faculty.departmentName}</div>
                                    </div>
                                </div>

                                <div className="col-md-6 col-lg-3 d-flex align-items-center gap-3">
                                    <div className="ch-stat-card__icon">
                                        <AwardIcon size={18} />
                                    </div>
                                    <div>
                                        <div className="ch-stat-card__label mb-1">Designation</div>
                                        <div className="fw-semibold">{faculty.designation}</div>
                                    </div>
                                </div>

                                <div className="col-md-6 col-lg-3 d-flex align-items-center gap-3">
                                    <div className="ch-stat-card__icon">
                                        <MailIcon size={18} />
                                    </div>
                                    <div>
                                        <div className="ch-stat-card__label mb-1">Email</div>
                                        <div className="fw-semibold text-truncate">{faculty.email}</div>
                                    </div>
                                </div>

                            </div>

                        </div>

                    </div>

                    <div className="row g-3">

                        <div className="col-sm-6 col-xl-3">
                            <DashboardCard
                                title="Assigned Courses"
                                value={faculty?.assignedCourses ?? 0}
                            />
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <DashboardCard
                                title="Students"
                                value={faculty?.totalStudents ?? 0}
                            />
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <DashboardCard
                                title="Attendance Records"
                                value={faculty?.attendanceRecords ?? 0}
                            />
                        </div>

                        <div className="col-sm-6 col-xl-3">
                            <DashboardCard
                                title="Marks Uploaded"
                                value={faculty?.marksUploaded ?? 0}
                            />
                        </div>

                    </div>
                </>
            )}

        </FacultyLayout>

    );

}

export default FacultyDashboard;
