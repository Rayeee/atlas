package com.eparty.ccp.web.framework;

import com.eparty.ccp.contract.exception.ServiceException;
import com.eparty.ccp.contract.exception.SystemException;
import com.joindata.inf.common.util.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class WebAPIBaseController {

    private static final Logger logger = Logger.get();

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public RestResponse<String> handleThrowable(Throwable e) {
        if (e instanceof ServiceException || e.getCause() instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) (e instanceof ServiceException ? e : e.getCause());
            logger.error("request请求【业务异常】code = {},msg = {}", serviceException.getCode(), serviceException.getMessage());
            if (StringUtils.isEmpty(serviceException.getCode())) {
                return RestResponse.fail(serviceException.getMessage());
            } else {
                return RestResponse.fail(serviceException.getCode(), serviceException.getMessage());
            }
        } else if (e instanceof SystemException || e.getCause() instanceof SystemException) {
            SystemException systemException = (SystemException) (e instanceof SystemException ? e : e.getCause());
            logger.error("request请求【系统异常】code = {},msg = {}", systemException.getCode(), systemException.getMessage());
            return RestResponse.fail(systemException.getCode(), e.getMessage());
        } else {
            logger.error("request请求【未知异常】", e);
            return RestResponse.fail(e.getMessage());
        }
    }

}
