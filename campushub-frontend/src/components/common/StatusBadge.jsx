/**
 * A small, consistent status pill. Pass either a known `status`
 * (PRESENT / ABSENT / grade letters) or an explicit `tone`.
 */
const STATUS_TONE = {
    PRESENT: "success",
    ABSENT: "danger",
    S: "success",
    A: "success",
    B: "info",
    C: "info",
    D: "warning",
    E: "warning",
    F: "danger",
    X: "danger",
};

function StatusBadge({ status, tone, children }) {
    const resolvedTone = tone || STATUS_TONE[status] || "neutral";

    return (
        <span className={`ch-badge ch-badge--${resolvedTone}`}>
            <span className="ch-badge-dot" />
            {children || status}
        </span>
    );
}

export default StatusBadge;
