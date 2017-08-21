package com.eparty.ccp.core.retry;

import com.eparty.ccp.core.constant.BaseMessageConstants;
import com.joindata.inf.common.basic.exceptions.BizException;
import com.joindata.inf.common.util.log.Logger;

public class RetryUtils {

    private static final Logger logger = Logger.get();

    public static <T> T retry(RetryFun<T> fun, int count, Class<?> clazz, String methodName) throws BizException {
        int i = 1;
        //使用do...while语句保证任务至少执行一次
        do {
            long start = System.currentTimeMillis();
            try {
                //执行任务
                T result = fun.execute();
                logger.info("第{}执行【{}】重试任务【成功】", i, methodName);
                return result;
            } catch (Exception e) {
                if (e instanceof BizException) {
                    logger.error("第【{}】次执行【{}】重试任务时【业务异常】msg={}不需重试", i, methodName, ((BizException) e).getErrorEntity().getMessage());
                    throw e;
                } else {
                    //结束时抛出异常
                    if (i >= count) {
                        logger.error(String.format("第【%s】次执行【%s】重试任务时【其它异常】不再重试", i, methodName), e);
                        throw e;
                    } else {
                        logger.error(String.format("第【%s】次执行【%s】重试任务时【其它异常】继续重试", i, methodName), e);
                    }
                }
            }
        } while (i++ < count);
        logger.error(String.format("执行【%s】重试任务时出现严重错误！！！", methodName));
        throw BaseMessageConstants.sys_error;
    }
}
