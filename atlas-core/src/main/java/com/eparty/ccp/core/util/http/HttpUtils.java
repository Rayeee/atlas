package com.eparty.ccp.core.util.http;

import com.eparty.ccp.core.util.JsonUtils;
import com.joindata.inf.common.util.log.Logger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final Logger logger = Logger.get();

    public static String doPostWithClient(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap) throws HttpStateException {
        return doPostWithClient(httpClient, url, paramsMap, null, null, null);
    }

    public static String doPostWithClient(CloseableHttpClient httpClient, String url, Map<String, String> paramsMap,
                                          Integer connTimeout, Integer reqTimeout, Integer socketTimeout) throws HttpStateException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
        if (connTimeout != null && reqTimeout != null && socketTimeout != null) {
            RequestConfig.Builder custom = RequestConfig.custom();
            custom.setConnectTimeout(connTimeout)
                    .setConnectionRequestTimeout(reqTimeout)
                    .setSocketTimeout(socketTimeout);
            RequestConfig requestConfig = custom.build();
            httpPost.setConfig(requestConfig);
        }
        if (!CollectionUtils.isEmpty(paramsMap)) {
            List<NameValuePair> params = new ArrayList<>(paramsMap.size());
            paramsMap.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("请求Http【编码转换异常】url = {}, param = {}", url, JsonUtils.toJson(paramsMap), e);
                throw new HttpStateException("500", "Http请求 UnsupportedEncodingException");
            }
        }
        return doRequestWithClient(httpClient, httpPost, paramsMap);
    }

    private static String doRequestWithClient(CloseableHttpClient httpClient, HttpRequestBase httpRequest, Map<String, String> paramsMap) throws HttpStateException {
        CloseableHttpResponse response = null;
        String method = httpRequest.getMethod();
        String url = httpRequest.getURI().toString();
        try {
            response = httpClient.execute(httpRequest);
            String responseStr = EntityUtils.toString(response.getEntity());
            logger.info("请求Http method = {},url = {}, param = {}, result = {}", method, url, JsonUtils.toJson(paramsMap), responseStr);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                //httpCode为200，需要在调用方区分正常返回和业务异常返回
                return responseStr;
            } else {
                int httpCode = response.getStatusLine().getStatusCode();
                String httpMsg = response.getStatusLine().getReasonPhrase();
                throw new HttpStateException(String.valueOf(httpCode), httpMsg);
            }
        } catch (IOException e) {
            logger.error("请求Http method = {}【IO异常,url = {}, param = {}", method, url, JsonUtils.toJson(paramsMap), e);
            throw new HttpStateException("500", "Http请求IOException");
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    logger.error("关闭Http出错 method = {}, url = {}, param = {}", method, url, JsonUtils.toJson(paramsMap), e);
                } finally {
                    response = null;
                }
            }
        }
    }

    /**
     * http get请求
     *
     * @param client
     * @param url
     * @param headers
     * @return
     */
    public static String doGetWithRequestParams(CloseableHttpClient client, String url, Header[] headers) {
        HttpRequestBase httpGet = new HttpGet(url);
        httpGet.setProtocolVersion(HttpVersion.HTTP_1_1);
        if (ArrayUtils.isNotEmpty(headers)) {
            //自定义请求头Header
            httpGet.setHeaders(headers);
        }
        return doRequestWithClient(client, httpGet, null);
    }

}
