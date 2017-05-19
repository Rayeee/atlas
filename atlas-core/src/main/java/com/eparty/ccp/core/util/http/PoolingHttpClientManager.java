package com.eparty.ccp.core.util.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

/**
 * Created by zhugongyi on 2017/5/19.
 */
public class PoolingHttpClientManager {

    private final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    private CloseableHttpClient client;

    public PoolingHttpClientManager(int maxConnTotal, int soTimeout, int connectionTimeout) {
        //设置连接数
        setCMMaxConnTotal(maxConnTotal);
        //默认配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout)
                .build();
        client = getHttpClient(defaultRequestConfig);
    }

    private void setCMMaxConnTotal(int maxConnTotal) {
        //设置连接数
        cm.setMaxTotal(maxConnTotal);
        //路由最大连接
        cm.setDefaultMaxPerRoute(maxConnTotal);
    }

    private CloseableHttpClient getHttpClient(RequestConfig defaultRequestConfig) {
        //Socket Config
        SocketConfig defaultSocketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .build();
        //HTTPv1.1 在请求时候设置
        return HttpClients.custom()
                .setConnectionManager(cm)
                //默认请求配置
                .setDefaultRequestConfig(defaultRequestConfig)
                .setDefaultSocketConfig(defaultSocketConfig)
                //Debug测试代理
//                .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost("localhost",8888)))
                .build();
    }

    public CloseableHttpClient getHttpClient() {
        return client;
    }

    public PoolStats getTotalStats() {
        return this.cm.getTotalStats();
    }

}
