import axios from "axios";
import { getToken } from "./authService";

const API_URL = "http://localhost:8081/api/departments";

const authHeader = () => ({
    headers: {
        Authorization: `Bearer ${getToken()}`
    }
});

export const getAllDepartments = async () => {
    const response = await axios.get(API_URL, authHeader());
    return response.data.data;
};

export const createDepartment = async (department) => {
    const response = await axios.post(API_URL, department, authHeader());
    return response.data.data;
};

export const updateDepartment = async (id, department) => {
    const response = await axios.put(
        `${API_URL}/${id}`,
        department,
        authHeader()
    );
    return response.data.data;
};

export const deleteDepartment = async (id) => {
    await axios.delete(`${API_URL}/${id}`, authHeader());
};