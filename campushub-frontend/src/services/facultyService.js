import api from "../api/axiosConfig";

export const getAllFaculties = async () => {
    const response = await api.get("/faculties");
    return response.data.data;
};

export const createFaculty = async (faculty) => {
    const response = await api.post("/faculties", faculty);
    return response.data.data;
};

export const updateFaculty = async (id, faculty) => {
    const response = await api.put(`/faculties/${id}`, faculty);
    return response.data.data;
};

export const deleteFaculty = async (id) => {
    const response = await api.delete(`/faculties/${id}`);
    return response.data.data;
};

export const getMyProfile = async () => {

    const response = await api.get("/faculties/me");

    return response.data.data;
};

export const getFacultyDashboard = async () => {

    const response = await api.get("/faculty/dashboard");

    return response.data.data;
};