package com.eparty.ccp.core.constant;

import com.joindata.inf.common.basic.exceptions.BizException;

public interface BaseMessageConstants {

    //系统定义异常
    BizException sys_error = new BizException(500, "系统异常");
}
