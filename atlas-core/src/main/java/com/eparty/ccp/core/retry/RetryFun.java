package com.eparty.ccp.core.retry;

import com.eparty.ccp.contract.exception.ServiceException;

@FunctionalInterface
public interface RetryFun<T> {

    T execute() throws ServiceException;

}
