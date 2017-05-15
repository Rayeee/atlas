package com.eparty.ccp.core.retry;

import com.eparty.ccp.contract.exception.ServiceException;

/**
 * Created by zhugongyi on 2017/5/10.
 */
@FunctionalInterface
public interface RetryFun<T> {

    T execute() throws ServiceException;

}
