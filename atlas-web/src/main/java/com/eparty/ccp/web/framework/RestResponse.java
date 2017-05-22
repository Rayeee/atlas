package com.eparty.ccp.web.framework;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by zhugongyi on 2017/5/22.
 */
public class RestResponse<E> {

    private int code;

    private E model;

    private Map<String, Object> extra = Maps.newHashMap();

    private String errorCode;

    private String message;

    public static final <E> RestResponse<E> success() {
        return new RestResponse<>(200, null, "success");
    }

    public static final <E> RestResponse<E> success(E model) {
        return new RestResponse<>(200, "success", model);
    }

    public static final <E> RestResponse<E> fail(String errCode, String message) {
        return new RestResponse<>(500, errCode, message);
    }

    public static final <E> RestResponse<E> fail(String message) {
        return new RestResponse<>(500, null, message);
    }

    private RestResponse() {

    }

    public RestResponse(int code, String message, E model) {
        this.code = code;
        this.message = message;
        this.model = model;
    }

    public RestResponse(int code, String errorCode, String message) {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public E getModel() {
        return model;
    }

    public void setModel(E model) {
        this.model = model;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
