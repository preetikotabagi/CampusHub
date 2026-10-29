import api from "../api/axiosConfig";

export const getDashboard = async () => {
    const response = await api.get("/dashboard");
    return response.data.data;
};
