package com.eparty.ccp.core.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zhugongyi on 2017/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:atlas-spring.xml")
public class MetricsTest {

    @Resource
    private TestService testService;

    @Test
    public void test0(){
        testService.test0(3);
    }

}
