package com.eparty.ccp.core.proxy;

import com.eparty.ccp.contract.exception.ServiceException;
import com.eparty.ccp.contract.exception.SystemException;
import com.eparty.ccp.core.util.DefaultValue;
import com.eparty.ccp.core.util.JsonUtils;
import com.joindata.inf.common.util.log.Logger;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
public class MetricsAspect {

    private Logger logger = Logger.get();

    public MetricsAspect() {
    }

    @Around("@annotation(com.eparty.ccp.core.proxy.Metrics))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            metrics = defaultMetrics;
        }
        String metricsName = signature.getDeclaringTypeName() + " - " + signature.getName();
        String inputJson = null;
        //记录入参
        if (metrics.logInput()) {
            inputJson = JsonUtils.toJson(point.getArgs());
            logger.info("{} 调用【{}】- 参数为【{}】", metrics.desc(), metricsName, inputJson);
        }
        //执行业务
        try {
            Object result = point.proceed();
            //记录返回值
            if (metrics.logOutput()) {
                logger.info("{} 调用【{}】成功 - 返回值为【{}】", metrics.desc(), metricsName, JsonUtils.toJson(result));
            }
            return result;
        } catch (ServiceException se) {
            if (StringUtils.isEmpty(inputJson)) {
                inputJson = JsonUtils.toJson(point.getArgs());
            }
            if (metrics.ignoreBusErr() || metrics.ignoreError()) {
                Object defaultResult = DefaultValue.getDefaultValue(signature.getReturnType());
                logger.error("{} 调用【{}】-【业务异常-忽略异常】-【参数={}】-【业务异常信息:code={},msg={}】-【返回默认值defaultResult={}】", metrics.desc(), metricsName, inputJson, se.getCode(), se.getMessage(), JsonUtils.toJson(defaultResult));
                return defaultResult;
            } else {
                logger.error("{} 调用【{}】-【业务异常】-【参数={}】-【业务异常信息:code={},msg={}】", metrics.desc(), metricsName, inputJson, se.getCode(), se.getMessage());
                throw new ServiceException(se.getCode(), se.getMessage(), se.getCause());
            }
        } catch (Exception e) {
            if (StringUtils.isEmpty(inputJson)) {
                inputJson = JsonUtils.toJson(point.getArgs());
            }
            if (metrics.ignoreSysErr() || metrics.ignoreError()) {
                Object defaultResult = DefaultValue.getDefaultValue(signature.getReturnType());
                logger.error("{} 调用【{}】-【系统异常-忽略异常】-【参数={}】-【返回默认值defaultResult={}】", metrics.desc(), metricsName, inputJson, JsonUtils.toJson(defaultResult), e);
                return defaultResult;
            } else {
                logger.error("{} 调用【{}】-【系统异常】-【参数={}】", metrics.desc(), metricsName, inputJson, e);
                throw e;
            }
        }
    }


    private static final Metrics defaultMetrics = new Metrics() {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public String desc() {
            return "";
        }

        @Override
        public boolean logInput() {
            return true;
        }

        @Override
        public boolean logOutput() {
            return true;
        }

        @Override
        public boolean ignoreBusErr() {
            return false;
        }

        @Override
        public boolean ignoreSysErr() {
            return false;
        }

        @Override
        public boolean ignoreError() {
            return false;
        }
    };

}
