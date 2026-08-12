import api from "../api/axiosConfig";

export const getAllMarks = async () => {
    const response = await api.get("/marks");
    return response.data.data;
};

export const saveMarks = async (marks) => {
    const response = await api.post("/marks/bulk", marks);
    return response.data.data;
};

export const getMarksByCourse = async (courseId) => {
    const response = await api.get(`/marks/course/${courseId}`);
    return response.data.data;
};