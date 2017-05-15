package com.eparty.ccp.redis.util;

/**
 * Created by Rayee on 2017/3/29.
 */
public class JsonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JsonException(String message, Throwable e) {
        super(message, e);
    }

    public JsonException() {
        super();
    }

}
