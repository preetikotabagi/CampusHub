import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8081/api",
    headers: {
        "Content-Type": "application/json",
    },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
});

// If the token is missing/expired/invalid, the backend now returns a clean 401
// (see GlobalExceptionHandler + JwtAuthenticationFilter). Previously nothing
// handled that globally, so an expired session just produced page-by-page
// failures instead of sending the user back to login. Individual pages still
// get the rejected promise in their own catch blocks - this only adds the
// session cleanup + redirect on top.
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            const isLoginRequest = error.config?.url?.includes("/auth/login");

            if (!isLoginRequest) {
                localStorage.clear();

                if (window.location.pathname !== "/") {
                    window.location.href = "/";
                }
            }
        }

        return Promise.reject(error);
    }
);

export default api;