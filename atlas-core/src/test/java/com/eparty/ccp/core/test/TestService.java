package com.eparty.ccp.core.test;

import com.eparty.ccp.core.proxy.Metrics;

/**
 * Created by zhugongyi on 2017/7/19.
 */
public class TestService {

    @Metrics(desc = "测试", logInput = true, logOutput = true)
    public int test0(int i) {
        System.out.println(i);
        return i;
    }

}
