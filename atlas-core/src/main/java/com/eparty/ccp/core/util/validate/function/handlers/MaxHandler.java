package com.eparty.ccp.core.util.validate.function.handlers;

import com.eparty.ccp.contract.annotation.Max;
import com.joindata.inf.common.basic.exceptions.ServiceException;

import java.lang.reflect.Field;

/**
 * Created by di on 19/7/2016.
 */
public class MaxHandler extends AbstractHandler{

    public <T, F extends Field, E extends ServiceException> void handle(T originBean, F field, boolean forceException, E exception, boolean isDeep) throws ServiceException {
        if (isDeep) deepCheck(originBean, field, forceException, exception);
        else normalCheck(originBean, field, forceException, exception);
    }

    private static <T, F extends Field, E extends ServiceException> void deepCheck(T originBean, F field, boolean forceException, E exception) throws ServiceException {
        System.out.println("not support deep check now.");
    }


    private static <T, F extends Field, E extends ServiceException> void normalCheck(T originBean, F field, boolean forceException, E exception) throws ServiceException {
        String beanName = originBean.getClass().getName();
        String fieldName = field.getName();
        boolean flag = false;

        field.setAccessible(true);

        try {
            Object o = field.get(originBean);
            if (o == null) throw new IllegalAccessException();

            Max maxAnnotation = field.getAnnotation(Max.class);
            if (maxAnnotation != null){
                if (o instanceof Integer){
                    if ((Integer)o > maxAnnotation.value()) flag = true;
                }else if (o instanceof Long){
                    if ((Long)o > maxAnnotation.value()) flag = true;
                }else {
                    throw new ClassCastException();
                }
                if (flag) throw forceException ? exception : new ServiceException(String.format("%s 's field :%s is bigger than max :%s!", beanName, fieldName,maxAnnotation.value()));
            }
        } catch (IllegalAccessException e) {
            throw forceException ? exception : new ServiceException(String.format("%s 's field :%s can not be null!", beanName, fieldName));
        } catch (ClassCastException e){
            throw forceException ? exception : new ServiceException(String.format("%s 's field :%s is not num!", beanName, fieldName));
        }
    }

}
