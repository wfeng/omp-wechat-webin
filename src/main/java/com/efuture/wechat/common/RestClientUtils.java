package com.efuture.wechat.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.entity.ServiceResponse;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Rest客户端工具
 *
 * Created by wfeng on 2019/6/24.
 */
@Component
public class RestClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(RestClientUtils.class);

    @Autowired
    private Environment env;

    @Autowired
    RestTemplate restTemplate;

    public static RestClientUtils getRestUtils() {
        return SpringBeanFactory.getBean("RestUtils", RestClientUtils.class);
    }

    public String queryServiceURI(String method) {
        return env.getProperty(method);
    }

    public interface requestCallback {
        // 发送
        Object onSend(String url, Object param) throws Exception;
    }

    public class defaultRequestCallback implements requestCallback {
        @Override
        public Object onSend(String url, Object param) {

            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.APPLICATION_JSON;
            headers.setContentType(type);

            if (param instanceof JSONObject) {
                HttpEntity<JSONObject> formEntity = new HttpEntity<>((JSONObject) param, headers);
                return restTemplate.postForObject(url, formEntity, String.class);
            } else {
                HttpEntity<String> formEntity = new HttpEntity<>(param.toString(), headers);
                return restTemplate.postForObject(url, formEntity, String.class);
            }
        }
    }

    public ServiceResponse sendRequest(Object session, String method, Object param) throws Exception {
        Object result = RestClientUtils.getRestUtils().sendRequest(session, method, param,
                new RestClientUtils.defaultRequestCallback());
        ServiceResponse response = null;
        if (result != null) {
            JSONObject json = JSON.parseObject((String) result);
            response = JSON.toJavaObject(json, ServiceResponse.class);
            String sb = method + " , " +
                    "RESPONSE: " + json.toJSONString() + " , " +
                    "SESSION:" + JSONObject.toJSONString(session);
            logger.info(sb);
        }
        if (response == null)
            throw new ServiceException(ResponseCode.EXCEPTION, (String) result);

        return response;
    }

    protected Object sendRequest(Object session, String method, Object param, requestCallback callback) throws Exception {
        String url;
        long start_time = 0;
        long borro_time = 0;
        // 转换参数
        ServiceSession ss = null;
        if (session instanceof ServiceSession)
            ss = (ServiceSession) session;
        if (session instanceof Long) {
            ss = new ServiceSession();
            ss.setEnt_id((Long) session);
        }
        try {
            // 根据method找到URL
            if (method.toLowerCase().startsWith("http"))
                url = method;
            else {
                url = queryServiceURI(method);
                if (StringUtils.isEmpty(url))
                    throw new Exception("can't find method service url:" + method);
            }

            if (url.indexOf("{ent_id}") > 0 && ss != null)
                url = url.replace("{ent_id}", String.valueOf(ss.getEnt_id()));
            if (url.indexOf("{user_id}") > 0 && ss != null)
                url = url.replace("{user_id}", String.valueOf(ss.getUser_id()));
            if (url.indexOf("{user_code}") > 0 && ss != null)
                url = url.replace("{user_code}", ss.getUser_code());
            if (url.indexOf("{user_name}") > 0 && ss != null)
                url = url.replace("{user_name}", ss.getUser_name());
            if (url.indexOf("{locale}") > 0 && ss != null)
                url = url.replace("{locale}", ss.getLocale());
            if (url.indexOf("{token}") > 0 && ss != null && ss.getToken() != null)
                url = url.replace("{token}", ss.getToken());

            // 记录转发日志
            start_time = System.currentTimeMillis();
            // 记录转发日志
            borro_time = System.currentTimeMillis();
            // 转发
            return callback.onSend(url, param);
        } catch (Exception ex) {
            StringBuilder sblog = new StringBuilder();
            sblog.append("[").append(ex.getMessage()).append("]: ");
            sblog.append(method).append(" , ");
            sblog.append("ELAPSED: ").append(System.currentTimeMillis() - start_time).append(" ms , ");
            sblog.append("BORROWS: ").append(borro_time - start_time).append(" ms , ");
            logger.info(sblog.toString());
            throw ex;
        } finally {
            // 记录转发日志
                StringBuilder sb = new StringBuilder();
                sb.append(method).append(" , ");
                sb.append("ELAPSED: ").append(System.currentTimeMillis() - start_time).append(" ms , ");
                sb.append("BORROWS: ").append(borro_time - start_time).append(" ms , ");
                sb.append("REQUEST: ").append(param).append(" , ");
                sb.append("SESSION:").append(JSONObject.toJSONString(session));
                logger.info(sb.toString());
        }
    }
}
