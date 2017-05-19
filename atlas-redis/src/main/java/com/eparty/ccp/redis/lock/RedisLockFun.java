package com.eparty.ccp.redis.lock;

import com.eparty.ccp.contract.exception.ServiceException;

@FunctionalInterface
public interface RedisLockFun {

    void execute() throws ServiceException;

}
