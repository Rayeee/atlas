package com.eparty.ccp.web.framework;

/**
 * Created by Rayee on 2017/3/31.
 */
public class ResponseEntity<E> {

    private String code;
    private String msg;
    private E data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public ResponseEntity(String code, String msg, E data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> ResponseEntity<E> success() {
        return success(null);
    }

    public static <E> ResponseEntity<E> success(E data) {
        return new ResponseEntity<>("200", "success", data);
    }

    public static <E> ResponseEntity<E> fail(String message) {
        return new ResponseEntity<>(null, message, null);
    }

    public static <E> ResponseEntity<E> fail(String code, String message) {
        return new ResponseEntity<>(code, message, null);
    }
}