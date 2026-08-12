import { InboxIcon } from "../icons/Icon";

function EmptyState({ title = "Nothing here yet", text, icon: IconComp = InboxIcon }) {
    return (
        <div className="ch-empty-state">
            <div className="ch-empty-state__icon">
                <IconComp size={24} />
            </div>
            <div className="ch-empty-state__title">{title}</div>
            {text && <p className="ch-empty-state__text">{text}</p>}
        </div>
    );
}

export default EmptyState;
