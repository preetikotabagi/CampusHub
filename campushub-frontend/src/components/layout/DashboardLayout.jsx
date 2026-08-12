import { useState } from "react";
import Sidebar from "./Sidebar";
import Navbar from "./Navbar";

function DashboardLayout({ role, children }) {
    const [sidebarOpen, setSidebarOpen] = useState(false);

    return (
        <div className="ch-shell">
            <Sidebar
                role={role}
                isOpen={sidebarOpen}
                onClose={() => setSidebarOpen(false)}
            />

            <div className="ch-main">
                <Navbar onMenuClick={() => setSidebarOpen(true)} />

                <main className="ch-content">{children}</main>
            </div>
        </div>
    );
}

export default DashboardLayout;
