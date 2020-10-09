package com.efuture.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.entity.ServiceResponse;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.exception.ServiceRuntimeException;
import com.efuture.wechat.utils.StatisticUtils;
import com.efuture.wechat.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @title: ProductController
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/16
 */
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    @Qualifier("ServiceMethodReflect")
    ProductReflect rcm; // 反射执行服务方法

    public String onRestService(String method,String session,String ent_id,String user_id,String user_name,String locale,String rootkey,String param) {
        // 计算统计信息
        long  startTimeMillis = System.currentTimeMillis();
        StatisticUtils.onIncrementMethod(method);

        LOGGER.info(String.format("---->method=%1$s session=%2$s param=%3$s ent_id=%4$s user_id=%5$s user_name=%6$s locale=%7$s",method+"",session+"",param+"",ent_id+"",user_id+"",user_name+"",locale+""));
        try {
            if (StringUtils.isEmpty(session) && !StringUtils.isEmpty(ent_id)) {
                ServiceSession ss = new ServiceSession();
                if (!StringUtils.isEmpty(ent_id)) ss.setEnt_id(Long.parseLong(ent_id));
                if (!StringUtils.isEmpty(user_id)) ss.setUser_id(Long.parseLong(user_id));
                ss.setUser_name(user_name);
                ss.setLocale(locale);
                session = JSON.toJSONString(ss);
            } else {
                ServiceSession ss = JSONObject.parseObject(session, ServiceSession.class);
                ss.setMethod(method);
                ss.setRootkey(rootkey);
                ss.setStarttime(startTimeMillis);
                session = JSON.toJSONString(ss);
            }

            //System.out.println(String.format("当前会话:%1$s\r\n请求方法:%2$s\r\n请求参数:%3$s", session,method,param));
            Object retdata = rcm.executeClassMethod(method, session, param);

            if (retdata == null) {
                return "";
            } else if (retdata instanceof String) {
                return (String) retdata;
            } else {
                return JSON.toJSONString(Utils.toNormalJSONObject(retdata));
            }
            //王朝阳   2018/10/11   处理掉断言引起异常  给前端友好的提示...
        } catch (InvocationTargetException e) {
            Throwable th = e.getTargetException();
            if(th instanceof IllegalArgumentException) {
                return JSON.toJSONString(ServiceResponse.buildFailure(null, ResponseCode.FAILURE,e.getTargetException().getMessage()));
            }else if(th instanceof ServiceRuntimeException) {
                ServiceRuntimeException ex = (ServiceRuntimeException) th;
                return JSON.toJSONString(ServiceResponse.buildFailure(null,ex.getCode(),ex.getMessage()));
            }
            return JSON.toJSONString(ServiceResponse.buildFailure(null,ResponseCode.FAILURE,Utils.getLastExceptionMessage(e)));
        } catch (Exception ex) {
            return JSON.toJSONString(ServiceResponse.buildFailure(null,ResponseCode.FAILURE,Utils.getLastExceptionMessage(ex)));
        } finally {
            long  endTimeMillis = System.currentTimeMillis();
            long  elapsed = endTimeMillis - startTimeMillis;
            // 计算统计信息
            StatisticUtils.onDecrementMethod(method, elapsed);
        }
    }

}
