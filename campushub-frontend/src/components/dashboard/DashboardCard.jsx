import {
    GraduationCapIcon,
    UsersIcon,
    BuildingIcon,
    BookIcon,
    CalendarIcon,
    AwardIcon,
    ClipboardListIcon,
} from "../icons/Icon";

const ICONS = {
    Students: GraduationCapIcon,
    Faculty: UsersIcon,
    Departments: BuildingIcon,
    Courses: BookIcon,
    "Attendance Records": CalendarIcon,
    "Marks Records": AwardIcon,
    "Assigned Courses": BookIcon,
    "Marks Uploaded": ClipboardListIcon,
};

function DashboardCard({ title, value }) {
    const IconComp = ICONS[title] || ClipboardListIcon;

    return (
        <div className="ch-stat-card">
            <div>
                <p className="ch-stat-card__label">{title}</p>
                <h2 className="ch-stat-card__value">{value}</h2>
            </div>

            <div className="ch-stat-card__icon">
                <IconComp size={20} />
            </div>
        </div>
    );
}

export default DashboardCard;
