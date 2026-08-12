function LoadingState({ label = "Loading..." }) {
    return (
        <div className="ch-loading">
            <div className="ch-spinner" />
            <span>{label}</span>
        </div>
    );
}

export default LoadingState;
