import api from "../api/axiosConfig";

export const getAllCourses = async () => {
    const response = await api.get("/courses");
    return response.data.data;
};

export const createCourse = async (course) => {
    const response = await api.post("/courses", course);
    return response.data.data;
};

export const updateCourse = async (id, course) => {
    const response = await api.put(`/courses/${id}`, course);
    return response.data.data;
};

export const deleteCourse = async (id) => {
    const response = await api.delete(`/courses/${id}`);
    return response.data.data;
};

export const getMyCourses = async () => {
    const response = await api.get("/courses/my-courses");
    return response.data.data;
};

export const getStudentsByCourse = async (courseId) => {
    const response = await api.get(`/courses/${courseId}/students`);
    return response.data.data;
};