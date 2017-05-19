package com.eparty.ccp.core.constant;

import com.eparty.ccp.contract.exception.SystemException;

public interface BaseMessageConstants {

    //异常code
    String unknow_error_code = "UNKNOW_ERROR";
    String sys_error_code = "SYSTEM_ERROR";

    //系统定义异常
    SystemException unknow_error = new SystemException(unknow_error_code, "未知异常");
    SystemException sys_error = new SystemException(sys_error_code, "系统异常");
}
