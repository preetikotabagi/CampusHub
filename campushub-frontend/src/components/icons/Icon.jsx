/**
 * A small, self-contained outline icon set used across the whole app.
 * Deliberately not a new npm dependency (network installs aren't
 * guaranteed at build time) — every icon here is a hand-picked,
 * consistent 24x24 stroke icon in the Feather/Lucide style.
 */
function base(children, props) {
    const { size = 18, strokeWidth = 1.9, className = "", ...rest } = props;
    return (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            width={size}
            height={size}
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth={strokeWidth}
            strokeLinecap="round"
            strokeLinejoin="round"
            className={className}
            aria-hidden="true"
            {...rest}
        >
            {children}
        </svg>
    );
}

export const GridIcon = (p) =>
    base(
        <>
            <rect x="3" y="3" width="7" height="7" rx="1.5" />
            <rect x="14" y="3" width="7" height="7" rx="1.5" />
            <rect x="14" y="14" width="7" height="7" rx="1.5" />
            <rect x="3" y="14" width="7" height="7" rx="1.5" />
        </>,
        p
    );

export const BuildingIcon = (p) =>
    base(
        <>
            <rect x="4" y="3" width="16" height="18" rx="1.5" />
            <path d="M9 8h1M14 8h1M9 12h1M14 12h1M9 16h1M14 16h1" />
            <path d="M10 21v-4h4v4" />
        </>,
        p
    );

export const GraduationCapIcon = (p) =>
    base(
        <>
            <path d="M2 9l10-5 10 5-10 5-10-5z" />
            <path d="M6 11.5V17c0 1.5 2.7 3 6 3s6-1.5 6-3v-5.5" />
            <path d="M22 9v6" />
        </>,
        p
    );

export const UsersIcon = (p) =>
    base(
        <>
            <circle cx="9" cy="8" r="3.2" />
            <path d="M3 20c0-3.3 2.7-6 6-6s6 2.7 6 6" />
            <circle cx="17.5" cy="9" r="2.6" />
            <path d="M21.5 20c0-2.7-1.9-5-4.5-5.7" />
        </>,
        p
    );

export const BookIcon = (p) =>
    base(
        <>
            <path d="M4 4.5C4 3.7 4.7 3 5.5 3H12v18H5.5c-.8 0-1.5-.7-1.5-1.5v-15z" />
            <path d="M20 4.5c0-.8-.7-1.5-1.5-1.5H12v18h6.5c.8 0 1.5-.7 1.5-1.5v-15z" />
        </>,
        p
    );

export const ClipboardListIcon = (p) =>
    base(
        <>
            <rect x="5" y="4" width="14" height="17" rx="1.5" />
            <path d="M9 3h6a1 1 0 011 1v1H8V4a1 1 0 011-1z" />
            <path d="M8.5 11h.01M8.5 15h.01" />
            <path d="M11.5 11h4M11.5 15h4" />
        </>,
        p
    );

export const CalendarIcon = (p) =>
    base(
        <>
            <rect x="3.5" y="4.5" width="17" height="16" rx="1.8" />
            <path d="M8 3v3M16 3v3M3.5 9.5h17" />
            <path d="M8 14l1.6 1.6L12.5 12.5" />
        </>,
        p
    );

export const EditIcon = (p) =>
    base(
        <>
            <path d="M12 20h9" />
            <path d="M16.5 3.5a2.1 2.1 0 013 3L7 19l-4 1 1-4L16.5 3.5z" />
        </>,
        p
    );

export const TrashIcon = (p) =>
    base(
        <>
            <path d="M4 7h16" />
            <path d="M9 7V5a1 1 0 011-1h4a1 1 0 011 1v2" />
            <path d="M6 7l1 13a1.5 1.5 0 001.5 1.4h7A1.5 1.5 0 0017 20l1-13" />
            <path d="M10 11v6M14 11v6" />
        </>,
        p
    );

export const LogOutIcon = (p) =>
    base(
        <>
            <path d="M9 21H6a2 2 0 01-2-2V5a2 2 0 012-2h3" />
            <path d="M16 17l5-5-5-5" />
            <path d="M21 12H9" />
        </>,
        p
    );

export const MenuIcon = (p) =>
    base(
        <>
            <path d="M4 6h16M4 12h16M4 18h16" />
        </>,
        p
    );

export const XIcon = (p) =>
    base(
        <>
            <path d="M6 6l12 12M18 6L6 18" />
        </>,
        p
    );

export const ChevronDownIcon = (p) =>
    base(
        <>
            <path d="M6 9l6 6 6-6" />
        </>,
        p
    );

export const UserIcon = (p) =>
    base(
        <>
            <circle cx="12" cy="8" r="3.4" />
            <path d="M4.5 20c0-4.1 3.4-7 7.5-7s7.5 2.9 7.5 7" />
        </>,
        p
    );

export const KeyIcon = (p) =>
    base(
        <>
            <circle cx="8" cy="15" r="4" />
            <path d="M11 12l8-8M16 4l3 3M18 6l2.5 2.5" />
        </>,
        p
    );

export const EyeIcon = (p) =>
    base(
        <>
            <path d="M2 12s3.8-7 10-7 10 7 10 7-3.8 7-10 7-10-7-10-7z" />
            <circle cx="12" cy="12" r="3" />
        </>,
        p
    );

export const EyeOffIcon = (p) =>
    base(
        <>
            <path d="M3 3l18 18" />
            <path d="M10.6 5.2A10.7 10.7 0 0112 5c6.2 0 10 7 10 7a15.6 15.6 0 01-4.2 4.9M6.6 6.6C4 8.3 2 12 2 12s3.8 7 10 7c1.4 0 2.6-.3 3.7-.8" />
            <path d="M9.9 9.9a3 3 0 004.2 4.2" />
        </>,
        p
    );

export const AlertCircleIcon = (p) =>
    base(
        <>
            <circle cx="12" cy="12" r="9" />
            <path d="M12 8v5M12 16h.01" />
        </>,
        p
    );

export const CheckCircleIcon = (p) =>
    base(
        <>
            <circle cx="12" cy="12" r="9" />
            <path d="M8.5 12.5l2.3 2.3L15.5 9" />
        </>,
        p
    );

export const InboxIcon = (p) =>
    base(
        <>
            <path d="M3.5 12.5h5l1.5 2.5h4l1.5-2.5h5" />
            <path d="M6 4.5h12l2.5 8v6a1.5 1.5 0 01-1.5 1.5h-14A1.5 1.5 0 013.5 18.5v-6l2.5-8z" />
        </>,
        p
    );

export const ChartBarIcon = (p) =>
    base(
        <>
            <path d="M4 20V10M11 20V4M18 20v-7" />
            <path d="M2.5 20h19" />
        </>,
        p
    );

export const TrendUpIcon = (p) =>
    base(
        <>
            <path d="M3 17l6-6 4 4 8-8" />
            <path d="M15 6h6v6" />
        </>,
        p
    );

export const AwardIcon = (p) =>
    base(
        <>
            <circle cx="12" cy="8" r="5.2" />
            <path d="M8.2 12.6L6.5 20l5.5-3 5.5 3-1.7-7.4" />
        </>,
        p
    );

export const IdCardIcon = (p) =>
    base(
        <>
            <rect x="3" y="5" width="18" height="14" rx="1.8" />
            <circle cx="8.5" cy="11" r="2" />
            <path d="M5.5 16c.5-1.6 1.7-2.5 3-2.5s2.5.9 3 2.5" />
            <path d="M14 9.5h5M14 12.5h5M14 15.5h3" />
        </>,
        p
    );

export const MailIcon = (p) =>
    base(
        <>
            <rect x="3" y="5" width="18" height="14" rx="1.8" />
            <path d="M3.5 6.5L12 13l8.5-6.5" />
        </>,
        p
    );

export const PlusIcon = (p) =>
    base(
        <>
            <path d="M12 5v14M5 12h14" />
        </>,
        p
    );

export const SaveIcon = (p) =>
    base(
        <>
            <path d="M5 4h11l3 3v13H5z" />
            <path d="M8 4v5h7V4" />
            <path d="M8 20v-6h8v6" />
        </>,
        p
    );

export const ArrowLeftIcon = (p) =>
    base(
        <>
            <path d="M19 12H5M11 6l-6 6 6 6" />
        </>,
        p
    );

export const BuildingLibraryIcon = GraduationCapIcon;
