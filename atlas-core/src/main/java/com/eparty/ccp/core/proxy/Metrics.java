package com.eparty.ccp.core.proxy;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {

    /**
     * 方法描述
     * @return
     */
    String desc() default "";

    /**
     * 是否打印入参，默认不打印
     * @return
     */
    boolean logInput() default false;

    /**
     * 是否打印出参，默认不打印
     * @return
     */
    boolean logOutput() default false;

    /**
     * 是否忽略业务异常，默认不忽略，抛出
     * @return
     */
    boolean ignoreBusErr() default false;

    /**
     * 是否忽略系统异常，默认不忽略，抛出
     * @return
     */
    boolean ignoreSysErr() default false;

    /**
     * 是否忽略异常，默认不忽略，抛出
     * @return
     */
    boolean ignoreError() default false;

}
