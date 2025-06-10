package com.cloudlibrary_api.common.exception;

import com.cloudlibrary_api.common.utils.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

// GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus httpStatus;
        
        // 根据错误码设置对应的HTTP状态码
        switch (errorCode) {
            case NOT_LOGIN:
            case UNAUTHORIZED:
            case INVALID_TOKEN:
            case MISSING_USER_INFO:
            case TOKEN_VERIFICATION_FAIL:
                httpStatus = HttpStatus.UNAUTHORIZED; // 401
                break;
            case FORBIDDEN:
                httpStatus = HttpStatus.FORBIDDEN; // 403
                break;
            case NOT_FOUND:
            case USER_NOT_FOUND:
            case BOOK_NOT_FOUND:
                httpStatus = HttpStatus.NOT_FOUND; // 404
                break;
            case PARAMS_ERROR:
                httpStatus = HttpStatus.BAD_REQUEST; // 400
                break;
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        }

        Result<Void> result = Result.error(errorCode.getCode(), errorCode.getMessage());
        return new ResponseEntity<>(result, httpStatus);
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        Result<Void> result = Result.error(ErrorCode.PARAMS_ERROR.getCode(), errorMsg);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理枚举转换异常
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        Result<Void> result = Result.error(ErrorCode.PARAMS_ERROR.getCode(), ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception ex) {
        Result<Void> result = Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请联系管理员");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}