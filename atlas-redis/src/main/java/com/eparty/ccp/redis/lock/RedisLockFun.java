package com.eparty.ccp.redis.lock;

import com.eparty.ccp.contract.exception.ServiceException;

/**
 * Created by Rayee on 2017/3/22.
 */
@FunctionalInterface
public interface RedisLockFun {

    void execute() throws ServiceException;

}
