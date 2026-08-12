import DashboardLayout from "./DashboardLayout";

function AdminLayout({ children }) {
    return <DashboardLayout role="ADMIN">{children}</DashboardLayout>;
}

export default AdminLayout;
