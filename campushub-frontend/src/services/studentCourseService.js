import api from "../api/axiosConfig";

export const enrollStudent = async (request) => {
    const response = await api.post("/student-courses", request);
    return response.data.data;
};

export const getMyEnrolledCourses = async () => {
    const response = await api.get("/student-courses/me");
    return response.data.data;
};

export const removeEnrollment = async (id) => {
    await api.delete(`/student-courses/${id}`);
};

export const getMyCourses = async () => {
    const response = await api.get("/students/my-courses");
    return response.data.data;
};