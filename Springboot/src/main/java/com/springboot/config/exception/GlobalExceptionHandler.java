package com.springboot.config.exception;

import com.springboot.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private void logError(Exception e, String msg, HttpServletRequest request) {
        String errorMessage = String.format("%s\nerror: %s\nurl: %s", msg, e.getMessage(), request.getRequestURL().toString());
        log.error(errorMessage);
    }


    @ExceptionHandler(value = {CustomException.class})
    @ResponseBody
    public R exceptionHandler(CustomException e, HttpServletRequest request) {
        logError(e, "自定义错误", request);
        return R.exception(e.getMessage());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public R exceptionHandler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        logError(e, "请求方法错误", request);
        return R.exception("请求方法错误");
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    public R exceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        logError(e, "权限错误", request);
        return R.permission_error("权限错误");
    }
}
