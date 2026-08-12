import { BrowserRouter, Routes, Route } from "react-router-dom";

import AdminCourseMarksPage from "./pages/admin/CourseMarksPage";
import MarksPage from "./pages/admin/MarksPage";
import LoginPage from "./pages/auth/LoginPage";
import AdminDashboard from "./pages/admin/AdminDashboard";
import StudentDashboard from "./pages/student/StudentDashboard";
import ProtectedRoute from "./routes/ProtectedRoute";
import DepartmentPage from "./pages/admin/DepartmentPage";
import StudentPage from "./pages/admin/StudentPage";
import FacultyPage from "./pages/admin/FacultyPage";
import CoursePage from "./pages/admin/CoursePage";
import AttendancePage from "./pages/admin/AttendancePage";
import ChangePasswordPage from "./pages/auth/ChangePasswordPage";
import FacultyDashboard from "./pages/faculty/FacultyDashboard";
import MyCoursesPage from "./pages/faculty/MyCoursesPage";
import FacultyAttendancePage from "./pages/faculty/FacultyAttendancePage";
import StudentEnrollmentPage from "./pages/admin/StudentEnrollmentPage";
import CourseAttendancePage from "./pages/faculty/CourseAttendancePage";
import FacultyMarksPage from "./pages/faculty/FacultyMarksPage";
import CourseMarksPage from "./pages/faculty/CourseMarksPage";
import AdminCourseAttendancePage from "./pages/admin/CourseAttendancePage";
import StudentCoursesPage from "./pages/student/StudentCoursesPage";
import StudentAttendancePage from "./pages/student/StudentAttendancePage";
import StudentMarksPage from "./pages/student/StudentMarksPage";
import StudentResultsPage from "./pages/student/StudentResultsPage";

function App() {

    return (
        <BrowserRouter>

            <Routes>

                <Route path="/" element={<LoginPage />} />
                <Route
                    path="/change-password"
                    element={<ChangePasswordPage />}
                />
                <Route
                    path="/admin/student-enrollment"
                    element={<StudentEnrollmentPage />}
                />
                <Route
                    path="/faculty/courses"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <MyCoursesPage />
                        </ProtectedRoute>
                    }
                />
                <Route path="/admin/faculties" element={<FacultyPage />} />
                <Route path="/admin/courses" element={<CoursePage />} />
                <Route
                    path="/admin/attendance"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <AdminCourseAttendancePage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/admin/courses/:courseId/attendance"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <AttendancePage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/dashboard"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <AdminDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/departments"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <DepartmentPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/marks"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <AdminCourseMarksPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/courses/:courseId/marks"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <MarksPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/admin/students"
                    element={
                        <ProtectedRoute allowedRole="ADMIN">
                            <StudentPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/faculty/dashboard"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <FacultyDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/faculty/attendance"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <FacultyAttendancePage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/faculty/marks"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <FacultyMarksPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/faculty/courses/:courseId/attendance"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <CourseAttendancePage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/faculty/courses/:courseId/marks"
                    element={
                        <ProtectedRoute allowedRole="FACULTY">
                            <CourseMarksPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/student/dashboard"
                    element={
                        <ProtectedRoute allowedRole="STUDENT">
                            <StudentDashboard />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/student/courses"
                    element={
                        <ProtectedRoute allowedRole="STUDENT">
                            <StudentCoursesPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/student/attendance"
                    element={
                        <ProtectedRoute allowedRole="STUDENT">
                            <StudentAttendancePage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/student/marks"
                    element={
                        <ProtectedRoute allowedRole="STUDENT">
                            <StudentMarksPage />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/student/results"
                    element={
                        <ProtectedRoute allowedRole="STUDENT">
                            <StudentResultsPage />
                        </ProtectedRoute>
                    }
                />

            </Routes>

        </BrowserRouter>
    );
}

export default App;