package com.eparty.ccp.core.retry;

import com.joindata.inf.common.basic.exceptions.BizException;

@FunctionalInterface
public interface RetryFun<T> {

    T execute() throws BizException;

}
