import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login, saveAuth } from "../../services/authService";
import {
    GraduationCapIcon,
    EyeIcon,
    EyeOffIcon,
    AlertCircleIcon,
    CheckCircleIcon,
} from "../../components/icons/Icon";

function LoginPage() {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        setError("");
        setLoading(true);

        try {

            const response = await login({
                email,
                password
            });

            saveAuth(
                response.data.token,
                response.data.role,
                response.data.passwordChanged
            );

            if (!response.data.passwordChanged) {
                navigate("/change-password");
            }
            else if (response.data.role === "ADMIN") {
                navigate("/admin/dashboard");
            }
            else if (response.data.role === "FACULTY") {
                navigate("/faculty/dashboard");
            }
            else {
                navigate("/student/dashboard");
            }

        } catch (err) {

            setError(err.response?.data?.message || "Invalid email or password. Please try again.");

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
                        One platform for your entire university.
                    </h1>

                    <p className="ch-auth-subline">
                        Departments, faculty, courses, attendance and results —
                        managed in one place for Admins, Faculty and Students.
                    </p>

                    <div className="mt-4">
                        <div className="ch-auth-feature">
                            <CheckCircleIcon size={16} />
                            Role-based access for Admin, Faculty and Students
                        </div>
                        <div className="ch-auth-feature">
                            <CheckCircleIcon size={16} />
                            Real-time attendance and marks tracking
                        </div>
                        <div className="ch-auth-feature">
                            <CheckCircleIcon size={16} />
                            SGPA / CGPA and semester-wise results
                        </div>
                    </div>
                </div>

                <div className="ch-auth-foot">
                    &copy; {new Date().getFullYear()} CampusHub. All rights reserved.
                </div>

            </div>

            <div className="ch-auth-screen__form-side">

                <form className="ch-auth-card" onSubmit={handleLogin} noValidate>

                    <div className="d-lg-none mb-4 d-flex align-items-center gap-2">
                        <div
                            className="ch-sidebar__mark"
                            style={{ width: 34, height: 34 }}
                        >
                            <GraduationCapIcon size={17} strokeWidth={2} />
                        </div>
                        <strong style={{ fontFamily: "var(--ch-font-display)" }}>
                            CampusHub
                        </strong>
                    </div>

                    <div className="ch-auth-card__eyebrow">Welcome back</div>
                    <h1 className="ch-auth-card__title">Sign in to your account</h1>
                    <p className="ch-auth-card__subtitle">
                        Enter your credentials to access your dashboard.
                    </p>

                    {error && (
                        <div className="ch-error-state mb-3">
                            <AlertCircleIcon size={17} />
                            <div className="ch-error-state__text">{error}</div>
                        </div>
                    )}

                    <div className="mb-3">
                        <label className="form-label" htmlFor="login-email">
                            Email address
                        </label>

                        <input
                            id="login-email"
                            type="email"
                            className="form-control"
                            placeholder="you@campushub.edu"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            autoComplete="username"
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label" htmlFor="login-password">
                            Password
                        </label>

                        <div className="ch-input-group">
                            <input
                                id="login-password"
                                type={showPassword ? "text" : "password"}
                                className="form-control"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                autoComplete="current-password"
                                required
                            />

                            <button
                                type="button"
                                className="ch-input-group__btn"
                                onClick={() => setShowPassword((s) => !s)}
                                aria-label={showPassword ? "Hide password" : "Show password"}
                                tabIndex={-1}
                            >
                                {showPassword ? <EyeOffIcon size={17} /> : <EyeIcon size={17} />}
                            </button>
                        </div>
                    </div>

                    <button
                        className="btn btn-primary w-100 mt-2"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? (
                            <span className="d-inline-flex align-items-center gap-2">
                                <span className="ch-spinner ch-spinner--sm" style={{ borderColor: "rgba(255,255,255,0.35)", borderTopColor: "#fff" }} />
                                Signing in...
                            </span>
                        ) : (
                            "Sign in"
                        )}
                    </button>

                </form>

            </div>

        </div>
    );
}

export default LoginPage;
