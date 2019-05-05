package com.huiketong.cofpasgers.exception;

import com.huiketong.cofpasgers.json.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AjaxExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public JSONResult defaultErrorHandler(HttpServletRequest request, Exception e) {
        return JSONResult.errorException(e.getMessage());
    }
}
