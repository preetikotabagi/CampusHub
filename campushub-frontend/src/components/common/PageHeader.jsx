function PageHeader({ title, subtitle, actions }) {
    return (
        <div className="ch-page-header">
            <div>
                <h2 className="ch-page-header__title">{title}</h2>
                {subtitle && <p className="ch-page-header__subtitle">{subtitle}</p>}
            </div>

            {actions && <div className="ch-page-header__actions">{actions}</div>}
        </div>
    );
}

export default PageHeader;
