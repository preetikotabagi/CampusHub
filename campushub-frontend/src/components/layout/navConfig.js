import {
    GridIcon,
    BuildingIcon,
    GraduationCapIcon,
    UsersIcon,
    BookIcon,
    ClipboardListIcon,
    CalendarIcon,
    AwardIcon,
} from "../icons/Icon";

export const NAV_ITEMS = {
    ADMIN: [
        { path: "/admin/dashboard", label: "Dashboard", icon: GridIcon },
        { path: "/admin/departments", label: "Departments", icon: BuildingIcon },
        { path: "/admin/students", label: "Students", icon: GraduationCapIcon },
        { path: "/admin/faculties", label: "Faculty", icon: UsersIcon },
        { path: "/admin/courses", label: "Courses", icon: BookIcon },
        {
            path: "/admin/student-enrollment",
            label: "Enrollment",
            icon: ClipboardListIcon,
        },
        { path: "/admin/attendance", label: "Attendance", icon: CalendarIcon },
        { path: "/admin/marks", label: "Marks", icon: AwardIcon },
    ],
    FACULTY: [
        { path: "/faculty/dashboard", label: "Dashboard", icon: GridIcon },
        { path: "/faculty/courses", label: "My Courses", icon: BookIcon },
        { path: "/faculty/attendance", label: "Attendance", icon: CalendarIcon },
        { path: "/faculty/marks", label: "Marks", icon: AwardIcon },
    ],
    STUDENT: [
        { path: "/student/dashboard", label: "Dashboard", icon: GridIcon },
        { path: "/student/courses", label: "My Courses", icon: BookIcon },
        { path: "/student/attendance", label: "Attendance", icon: CalendarIcon },
        { path: "/student/marks", label: "Marks", icon: ClipboardListIcon },
        { path: "/student/results", label: "Results", icon: AwardIcon },
    ],
};

export const ROLE_LABEL = {
    ADMIN: "Administrator",
    FACULTY: "Faculty",
    STUDENT: "Student",
};

/**
 * Resolves a page title/breadcrumb for the topbar from the current
 * pathname. Falls back gracefully for dynamic (:id) routes.
 */
export function resolvePageTitle(pathname) {
    const table = {
        "/admin/dashboard": ["Dashboard", "Overview"],
        "/admin/departments": ["Departments", "Admin"],
        "/admin/students": ["Students", "Admin"],
        "/admin/faculties": ["Faculty", "Admin"],
        "/admin/courses": ["Courses", "Admin"],
        "/admin/student-enrollment": ["Student Enrollment", "Admin"],
        "/admin/attendance": ["Attendance", "Admin"],
        "/admin/marks": ["Marks", "Admin"],
        "/faculty/dashboard": ["Dashboard", "Overview"],
        "/faculty/courses": ["My Courses", "Faculty"],
        "/faculty/attendance": ["Attendance", "Faculty"],
        "/faculty/marks": ["Marks", "Faculty"],
        "/student/dashboard": ["Dashboard", "Overview"],
        "/student/courses": ["My Courses", "Student"],
        "/student/attendance": ["My Attendance", "Student"],
        "/student/marks": ["My Marks", "Student"],
        "/student/results": ["My Results", "Student"],
    };

    if (table[pathname]) return table[pathname];

    if (pathname.includes("/attendance")) return ["Attendance", "Course"];
    if (pathname.includes("/marks")) return ["Marks", "Course"];

    return ["CampusHub", ""];
}
