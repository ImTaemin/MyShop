package com.myshop.api.controller;

import com.myshop.api.domain.dto.response.BaseResponse;
import com.myshop.api.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = EmptyPasswordException.class)
    public ResponseEntity<BaseResponse> emptyPasswordHandler(EmptyPasswordException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = FileNameException.class)
    public ResponseEntity<BaseResponse> fileNameHandler(FileNameException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = GCPFileException.class)
    public ResponseEntity<BaseResponse> gcpFileHandler(GCPFileException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = InvalidFileTypeException.class)
    public ResponseEntity<BaseResponse> invalidFileTypeHandler(InvalidFileTypeException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<BaseResponse> itemNotFoundHandler(ItemNotFoundException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = NotExistFavoriteException.class)
    public ResponseEntity<BaseResponse> notExistFavoriteHandler(NotExistFavoriteException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = NotExistUserException.class)
    public ResponseEntity<BaseResponse> notExistUserHandler(NotExistUserException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = PasswordNotMatchException.class)
    public ResponseEntity<BaseResponse> passwordNotMatchHandler(PasswordNotMatchException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = PrivilegeNotPossessionException.class)
    public ResponseEntity<BaseResponse> privilegeNotPossessionHandler(PrivilegeNotPossessionException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<BaseResponse> userNameNotFoundHandler(UsernameNotFoundException e) {
        return BaseResponse.error(e.getMessage());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse> allSQLExceptionHandler(DataIntegrityViolationException e) {
        return BaseResponse.error("잘못된 값입니다.");
    }

    @ExceptionHandler(value = DuplicateCouponException.class)
    public ResponseEntity<BaseResponse> duplicateCouponHandler(DuplicateCouponException e) {
        return BaseResponse.error(e.getMessage());
    }

    // Exception.class 만 넣으면 처리가 안된다. 왜지?
    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ResponseEntity<BaseResponse> runtimeExceptionHandler(RuntimeException e) {
        return BaseResponse.error("오류");
    }

}
