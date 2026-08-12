import api from "../api/axiosConfig";

export const getAllAttendance = async () => {
    const response = await api.get("/attendance");
    return response.data.data;
};

export const createAttendance = async (attendance) => {
    const response = await api.post("/attendance", attendance);
    return response.data.data;
};

export const updateAttendance = async (id, attendance) => {
    const response = await api.put(`/attendance/${id}`, attendance);
    return response.data.data;
};

export const deleteAttendance = async (id) => {
    const response = await api.delete(`/attendance/${id}`);
    return response.data.data;
};

export const saveAttendance = async (attendanceList) => {
    await api.post("/attendance/bulk", attendanceList);
};

export const getAttendanceByCourseAndDate = async (
    courseId,
    attendanceDate
) => {

    const response = await api.get(
        `/attendance/course/${courseId}`,
        {
            params: {
                attendanceDate
            }
        }
    );

    return response.data.data;
};