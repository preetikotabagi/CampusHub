import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
    changePassword,
    getRole,
    setPasswordChanged
} from "../../services/authService";
import {
    GraduationCapIcon,
    KeyIcon,
    EyeIcon,
    EyeOffIcon,
    AlertCircleIcon,
    CheckCircleIcon,
} from "../../components/icons/Icon";

function ChangePasswordPage() {

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [showCurrent, setShowCurrent] = useState(false);
    const [showNew, setShowNew] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const navigate = useNavigate();

    const handleChangePassword = async (e) => {
        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            await changePassword({
                currentPassword,
                newPassword
            });

            setPasswordChanged();
            setSuccess(true);

            const role = getRole();

            setTimeout(() => {
                if (role === "ADMIN") {
                    navigate("/admin/dashboard");
                }
                else if (role === "FACULTY") {
                    navigate("/faculty/dashboard");
                }
                else {
                    navigate("/student/dashboard");
                }
            }, 600);

        } catch (err) {

            setError(
                err.response?.data?.message ||
                "Failed to change password. Please check your current password."
            );

        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="ch-auth-screen">

            <div className="ch-auth-screen__side d-none d-lg-flex">

                <div className="ch-auth-brand">
                    <div className="ch-sidebar__mark">
                        <GraduationCapIcon size={20} strokeWidth={2} />
                    </div>
                    <div>
                        <div className="ch-auth-brand__title">CampusHub</div>
                        <div className="ch-auth-brand__sub">Campus Management System</div>
                    </div>
                </div>

                <div>
                    <h1 className="ch-auth-headline">
                        Let's secure your account.
                    </h1>

                    <p className="ch-auth-subline">
                        You're signing in for the first time. Choose a new
                        password only you know before continuing to your
                        dashboard.
                    </p>
                </div>

                <div className="ch-auth-foot">
                    &copy; {new Date().getFullYear()} CampusHub. All rights reserved.
                </div>

            </div>

            <div className="ch-auth-screen__form-side">

                <form className="ch-auth-card" onSubmit={handleChangePassword} noValidate>

                    <div className="ch-avatar-ring mb-3">
                        <KeyIcon size={24} />
                    </div>

                    <div className="ch-auth-card__eyebrow">Account security</div>
                    <h1 className="ch-auth-card__title">Change your password</h1>
                    <p className="ch-auth-card__subtitle">
                        For your security, please set a new password to continue.
                    </p>

                    {error && (
                        <div className="ch-error-state mb-3">
                            <AlertCircleIcon size={17} />
                            <div className="ch-error-state__text">{error}</div>
                        </div>
                    )}

                    {success && (
                        <div className="alert alert-success d-flex align-items-center gap-2 mb-3">
                            <CheckCircleIcon size={17} />
                            Password changed. Redirecting to your dashboard...
                        </div>
                    )}

                    <div className="mb-3">
                        <label className="form-label">Current password</label>

                        <div className="ch-input-group">
                            <input
                                type={showCurrent ? "text" : "password"}
                                className="form-control"
                                placeholder="Temporary / current password"
                                value={currentPassword}
                                onChange={(e) => setCurrentPassword(e.target.value)}
                                autoComplete="current-password"
                                required
                            />

                            <button
                                type="button"
                                className="ch-input-group__btn"
                                onClick={() => setShowCurrent((s) => !s)}
                                tabIndex={-1}
                                aria-label="Toggle current password visibility"
                            >
                                {showCurrent ? <EyeOffIcon size={17} /> : <EyeIcon size={17} />}
                            </button>
                        </div>
                    </div>

                    <div className="mb-3">
                        <label className="form-label">New password</label>

                        <div className="ch-input-group">
                            <input
                                type={showNew ? "text" : "password"}
                                className="form-control"
                                placeholder="Choose a new password"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                autoComplete="new-password"
                                required
                            />

                            <button
                                type="button"
                                className="ch-input-group__btn"
                                onClick={() => setShowNew((s) => !s)}
                                tabIndex={-1}
                                aria-label="Toggle new password visibility"
                            >
                                {showNew ? <EyeOffIcon size={17} /> : <EyeIcon size={17} />}
                            </button>
                        </div>
                    </div>

                    <button
                        className="btn btn-primary w-100 mt-2"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? "Updating..." : "Change password"}
                    </button>

                </form>

            </div>

        </div>
    );
}

export default ChangePasswordPage;
