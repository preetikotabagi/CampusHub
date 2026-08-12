import { Navigate } from "react-router-dom";
import { getToken, getRole } from "../services/authService";

function ProtectedRoute({ children, allowedRole }) {

    const token = getToken();
    const role = getRole();

    if (!token) {
        return <Navigate to="/" replace />;
    }

    if (role !== allowedRole) {
        return <Navigate to="/" replace />;
    }

    return children;
}

export default ProtectedRoute;