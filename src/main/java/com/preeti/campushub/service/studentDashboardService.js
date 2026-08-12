import api from "../api/axiosConfig";

export const getMyProfile = async () => {
    const response = await api.get("/students/my-profile");
    return response.data.data;
};

export const getMyCourses = async () => {
    const response = await api.get("/students/my-courses");
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