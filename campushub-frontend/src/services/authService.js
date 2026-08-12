import api from "../api/axiosConfig";

export const login = (loginData) => {
    return api.post("/auth/login", loginData);
};

export const saveAuth = (token, role, passwordChanged) => {

    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("passwordChanged", passwordChanged);

};

export const getToken = () => {
    return localStorage.getItem("token");
};

export const getRole = () => {
    return localStorage.getItem("role");
};

export const isPasswordChanged = () => {
    return localStorage.getItem("passwordChanged") === "true";
};

export const logout = () => {
    localStorage.clear();
};

export const changePassword = (passwordData) => {
    return api.post("/auth/change-password", passwordData);
};

export const setPasswordChanged = () => {
    localStorage.setItem("passwordChanged", "true");
};