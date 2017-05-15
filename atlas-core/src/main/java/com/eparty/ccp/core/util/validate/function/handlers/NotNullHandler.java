package com.eparty.ccp.core.util.validate.function.handlers;

import com.eparty.ccp.contract.exception.ServiceException;

import java.lang.reflect.Field;

/**
 * Created by Rayee on 2017/3/22.
 */
public class NotNullHandler extends AbstractHandler{

    public <T, F extends Field, E extends ServiceException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) throws ServiceException {
        if (isDeep) {
            deepCheck(originBean, field, forceException, exception);
        } else {
            normalCheck(originBean, field, forceException, exception);
        }
    }

    private static <T, F extends Field, E extends ServiceException> void deepCheck(T originBean, F field, boolean forceException, E exception) {
        System.out.println("not support deep check now.");
    }


    private static <T, F extends Field, E extends ServiceException> void normalCheck(T originBean, F field, boolean forceException, E exception) throws ServiceException {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();

        boolean isNull = false;
        try {
            field.setAccessible(true);
            Object o = field.get(originBean);
            if (o == null) isNull = true;
        } catch (IllegalAccessException e) {
            isNull = true;
        }

        if (isNull) {
            throw forceException ? exception : new ServiceException(String.format("%s 's field :%s can not be null!", beanName, fieldName));
        }
    }

}
