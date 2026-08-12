import { useEffect, useRef, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { getRole, logout } from "../../services/authService";
import { resolvePageTitle, ROLE_LABEL } from "./navConfig";
import { ChevronDownIcon, KeyIcon, LogOutIcon, MenuIcon, UserIcon } from "../icons/Icon";

function Navbar({ onMenuClick }) {
    const navigate = useNavigate();
    const location = useLocation();
    const [menuOpen, setMenuOpen] = useState(false);
    const menuRef = useRef(null);

    const role = getRole();
    const roleLabel = ROLE_LABEL[role] || role || "Account";
    const [title, crumb] = resolvePageTitle(location.pathname);

    useEffect(() => {
        function handleClickOutside(e) {
            if (menuRef.current && !menuRef.current.contains(e.target)) {
                setMenuOpen(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        setMenuOpen(false);
    }, [location.pathname]);

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <header className="ch-topbar">
            <div className="ch-topbar__left">
                <button
                    type="button"
                    className="ch-topbar__menu-btn"
                    onClick={onMenuClick}
                    aria-label="Open menu"
                >
                    <MenuIcon size={18} />
                </button>

                <div>
                    <h1 className="ch-topbar__title">{title}</h1>
                    {crumb && (
                        <div className="ch-topbar__crumb">
                            <span>CampusHub</span>
                            <span>/</span>
                            <span>{crumb}</span>
                        </div>
                    )}
                </div>
            </div>

            <div className="ch-topbar__right">
                <div className="ch-user-menu" ref={menuRef}>
                    <button
                        type="button"
                        className="ch-user-menu__trigger"
                        onClick={() => setMenuOpen((o) => !o)}
                    >
                        <span className="ch-user-menu__avatar">
                            {roleLabel.charAt(0)}
                        </span>

                        <span className="ch-user-menu__label">
                            <span className="ch-user-menu__role d-block">
                                {roleLabel}
                            </span>
                            <span className="ch-user-menu__hint">CampusHub</span>
                        </span>

                        <ChevronDownIcon size={15} className="ch-muted" />
                    </button>

                    <div className={`ch-user-menu__dropdown ${menuOpen ? "is-open" : ""}`}>
                        <div className="ch-user-menu__item" style={{ cursor: "default" }}>
                            <UserIcon size={15} />
                            Signed in as {roleLabel}
                        </div>

                        <div className="ch-user-menu__divider" />

                        <button
                            type="button"
                            className="ch-user-menu__item"
                            onClick={() => navigate("/change-password")}
                        >
                            <KeyIcon size={15} />
                            Change password
                        </button>

                        <button
                            type="button"
                            className="ch-user-menu__item ch-user-menu__item--danger"
                            onClick={handleLogout}
                        >
                            <LogOutIcon size={15} />
                            Logout
                        </button>
                    </div>
                </div>
            </div>
        </header>
    );
}

export default Navbar;
