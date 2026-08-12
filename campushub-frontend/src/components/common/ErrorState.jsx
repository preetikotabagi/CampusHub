import { AlertCircleIcon } from "../icons/Icon";

function ErrorState({ message = "Something went wrong. Please try again.", onRetry }) {
    return (
        <div className="ch-error-state">
            <AlertCircleIcon size={18} />
            <div className="ch-error-state__text">{message}</div>
            {onRetry && (
                <button
                    type="button"
                    className="btn btn-sm btn-secondary"
                    onClick={onRetry}
                >
                    Retry
                </button>
            )}
        </div>
    );
}

export default ErrorState;
