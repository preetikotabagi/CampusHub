import { Link, useLocation, useNavigate } from "react-router-dom";
import { GraduationCapIcon, LogOutIcon, XIcon } from "../icons/Icon";
import { NAV_ITEMS, ROLE_LABEL } from "./navConfig";
import { getRole, logout } from "../../services/authService";

function Sidebar({ role, isOpen, onClose }) {
    const location = useLocation();
    const navigate = useNavigate();
    const items = NAV_ITEMS[role] || [];
    const roleLabel = ROLE_LABEL[role] || getRole() || "User";

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <>
            <div
                className={`ch-sidebar__backdrop ${isOpen ? "is-open" : ""}`}
                onClick={onClose}
            />

            <aside className={`ch-sidebar ${isOpen ? "is-open" : ""}`}>
                <div className="ch-sidebar__brand">
                    <div className="ch-sidebar__mark">
                        <GraduationCapIcon size={20} strokeWidth={2} />
                    </div>

                    <div className="ch-sidebar__brand-text">
                        <div className="ch-sidebar__brand-title">CampusHub</div>
                        <div className="ch-sidebar__brand-sub">Campus ERP</div>
                    </div>

                    <button
                        type="button"
                        className="ch-topbar__menu-btn d-lg-none ms-auto"
                        style={{
                            display: "flex",
                            marginLeft: "auto",
                            background: "rgba(255,255,255,0.08)",
                            borderColor: "rgba(255,255,255,0.14)",
                            color: "#cdd6e8",
                        }}
                        onClick={onClose}
                        aria-label="Close menu"
                    >
                        <XIcon size={16} />
                    </button>
                </div>

                <nav className="ch-sidebar__nav">
                    <div className="ch-sidebar__section-label">Menu</div>

                    {items.map((item) => {
                        const ItemIcon = item.icon;
                        const active = location.pathname === item.path;

                        return (
                            <Link
                                key={item.path}
                                to={item.path}
                                onClick={onClose}
                                className={`ch-sidebar__link ${active ? "is-active" : ""}`}
                            >
                                <ItemIcon size={17} />
                                {item.label}
                            </Link>
                        );
                    })}
                </nav>

                <div className="ch-sidebar__footer">
                    <div className="ch-sidebar__user">
                        <div className="ch-sidebar__user-avatar">
                            {roleLabel.charAt(0)}
                        </div>
                        <div>
                            <div className="ch-sidebar__user-name">{roleLabel}</div>
                            <div className="ch-sidebar__user-role">CampusHub account</div>
                        </div>
                    </div>

                    <button
                        type="button"
                        className="ch-sidebar__logout"
                        onClick={handleLogout}
                    >
                        <LogOutIcon size={15} />
                        Logout
                    </button>
                </div>
            </aside>
        </>
    );
}

export default Sidebar;
