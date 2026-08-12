import axios from "axios";
import { getToken } from "./authService";

const API_URL = "http://localhost:8081/api/dashboard";

export const getDashboard = async () => {

    const response = await axios.get(API_URL, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });

    return response.data.data;
};