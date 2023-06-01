package com.c3stones.json.mapper.exception;

/**
 * JSONSQL 异常
 *
 * @author CL
 */
public class JQLException extends RuntimeException {

    private final ErrorCode errorCode;

    public JQLException(ErrorCode errorCode) {
        super(errorCode.getReason());
        this.errorCode = errorCode;
    }
    public JQLException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorCode.getReason() + "， 建议：" + this.errorCode.getSuggest();
    }

}
