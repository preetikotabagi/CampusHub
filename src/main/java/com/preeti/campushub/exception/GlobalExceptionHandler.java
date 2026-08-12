package com.preeti.campushub.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.preeti.campushub.common.ApiResponse;
import com.preeti.campushub.exception.attendance.DuplicateAttendanceException;
import com.preeti.campushub.exception.auth.UserAlreadyExistsException;
import com.preeti.campushub.exception.common.DuplicateEmailException;
import com.preeti.campushub.exception.common.ResourceNotFoundException;
import com.preeti.campushub.exception.course.DuplicateCourseCodeException;
import com.preeti.campushub.exception.course.InvalidFacultyDepartmentException;
import com.preeti.campushub.exception.department.DepartmentAlreadyExistsException;
import com.preeti.campushub.exception.faculty.DuplicateEmployeeIdException;
import com.preeti.campushub.exception.marks.DuplicateMarksException;
import com.preeti.campushub.exception.student.DuplicateUsnException;
import com.preeti.campushub.exception.student.InvalidProfilePictureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation Failed")
                        .data(errors)
                        .build());
    }
    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleDepartmentAlreadyExistsException(
            DepartmentAlreadyExistsException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(DuplicateUsnException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateUsnException(
                DuplicateUsnException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(DuplicateEmailException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateEmailException(
                DuplicateEmailException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(DuplicateEmployeeIdException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateEmployeeIdException(
                DuplicateEmployeeIdException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(DuplicateCourseCodeException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateCourseCodeException(
                DuplicateCourseCodeException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(InvalidFacultyDepartmentException.class)
        public ResponseEntity<ApiResponse<Void>> handleInvalidFacultyDepartmentException(
                InvalidFacultyDepartmentException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }
        @ExceptionHandler(DuplicateAttendanceException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateAttendanceException(
                DuplicateAttendanceException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }
        @ExceptionHandler(DuplicateMarksException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateMarksException(
                DuplicateMarksException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        @ExceptionHandler(InvalidProfilePictureException.class)
        public ResponseEntity<ApiResponse<Void>> handleInvalidProfilePictureException(
                InvalidProfilePictureException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        // Previously unhandled: a STUDENT/FACULTY hitting an ADMIN-only endpoint threw
        // AccessDeniedException, which fell through to Spring's default (non-JSON)
        // error page instead of the app's ApiResponse contract.
        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
                AccessDeniedException exception) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("You do not have permission to perform this action")
                        .build());
        }

        // Covers BadCredentialsException (wrong email/password on login) and any other
        // AuthenticationException - previously unhandled, so a wrong-password login
        // attempt returned a raw 500 instead of a clean 401.
        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
                AuthenticationException exception) {

        String message = exception instanceof BadCredentialsException
                ? "Invalid email or password"
                : "Authentication failed";

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(message)
                        .build());
        }

        // Covers e.g. AuthService.changePassword() throwing IllegalArgumentException
        // for an incorrect current password - previously unhandled (raw 500).
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
                IllegalArgumentException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(exception.getMessage())
                        .build());
        }

        // Malformed / unparseable JSON request bodies.
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
                HttpMessageNotReadableException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Malformed request body")
                        .build());
        }

        // Catch-all so an unexpected server error (e.g. a bug, a DB constraint we
        // didn't anticipate) still returns the app's standard ApiResponse JSON shape
        // instead of Spring's default whitelabel HTML error page - and so we never
        // leak an internal stack trace/message to the client.
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(
                Exception exception) {

        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class)
                .error("Unhandled exception", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Something went wrong. Please try again later.")
                        .build());
        }
}