import api from "../api/axiosConfig";

export const getAllDepartments = async () => {
    const response = await api.get("/departments");
    return response.data.data;
};

export const createDepartment = async (department) => {
    const response = await api.post("/departments", department);
    return response.data.data;
};

export const updateDepartment = async (id, department) => {
    const response = await api.put(`/departments/${id}`, department);
    return response.data.data;
};

export const deleteDepartment = async (id) => {
    await api.delete(`/departments/${id}`);
};
