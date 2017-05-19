package com.eparty.ccp.core.util.http;

import com.eparty.ccp.contract.exception.SystemException;

public class HttpStateException extends SystemException {

    public HttpStateException(String code, String message) {
        super(code, message);
    }
}
