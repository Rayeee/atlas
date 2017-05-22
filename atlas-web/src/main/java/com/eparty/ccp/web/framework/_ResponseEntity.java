package com.eparty.ccp.web.framework;

public class _ResponseEntity<E> {

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

    public _ResponseEntity(String code, String msg, E data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> _ResponseEntity<E> success() {
        return success(null);
    }

    public static <E> _ResponseEntity<E> success(E data) {
        return new _ResponseEntity<>("200", "success", data);
    }

    public static <E> _ResponseEntity<E> fail(String message) {
        return new _ResponseEntity<>(null, message, null);
    }

    public static <E> _ResponseEntity<E> fail(String code, String message) {
        return new _ResponseEntity<>(code, message, null);
    }
}
