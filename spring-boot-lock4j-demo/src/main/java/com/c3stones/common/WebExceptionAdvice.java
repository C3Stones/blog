package com.c3stones.common;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author CL
 */
@Log4j2
@RestControllerAdvice
public class WebExceptionAdvice {

    /**
     * 异常处理
     *
     * @param ex 异常
     * @return {@link R}
     */
    @ExceptionHandler(value = Exception.class)
    public R<?> errorHandler(Exception ex) {
        log.error(ex);
        return R.failed(ex.getMessage());
    }

}