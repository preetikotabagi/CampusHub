import api from "../api/axiosConfig";

export const getAllStudents = () =>
    api.get("/students");

export const createStudent = (student) =>
    api.post("/students", student);

export const updateStudent = (id, student) =>
    api.put(`/students/${id}`, student);

export const deleteStudent = (id) =>
    api.delete(`/students/${id}`);

export const fetchStudents = async () => {
    const response = await api.get("/students");
    return response.data.data;
};

export const getMyProfile = async () => {
    const response = await api.get("/students/me");
    return response.data.data;
};

export const getMyAttendance = async () => {
    const response = await api.get("/students/my-attendance");
    return response.data.data;
};

export const getMyMarks = async () => {
    const response = await api.get("/students/my-marks");
    return response.data.data;
};

export const getMyGpa = async () => {
    const response = await api.get("/analytics/my-sgpa-cgpa");
    return response.data.data;
};