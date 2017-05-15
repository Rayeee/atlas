package com.eparty.ccp.contract.exception;

/**
 * Created by Rayee on 2017/3/20.
 */

public class SystemException extends RuntimeException {

    private String code;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String code, String message) {
        this(message);
        this.setCode(code);
    }

    public SystemException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public SystemException(String code, Throwable cause) {
        super(cause);
        this.setCode(code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
