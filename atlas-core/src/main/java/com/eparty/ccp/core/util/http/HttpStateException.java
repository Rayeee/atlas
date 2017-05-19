package com.eparty.ccp.core.util.http;


import com.eparty.ccp.contract.exception.SystemException;

/**
 * Created by Rayee on 16/10/19.
 */
public class HttpStateException extends SystemException {

    public HttpStateException(String code, String message) {
        super(code, message);
    }
}
