package com.efuture.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by huangzhengwei on 2018/7/25.
 *
 * @Desciption:
 */
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    public static void formatJsonObject(ServiceSession session, JSONObject paramsObject){
        /**
         * JSONobject 添加默认信息
         * @param session
         * @param paramsObject
         * @return void
         * @throws
         */
        if(session != null) {
            paramsObject.put("entId", session.getEnt_id());
            paramsObject.put("creator", session.getUser_name());
            paramsObject.put("modifier", session.getUser_name());
            paramsObject.put("createDate", new Date());
            paramsObject.put("updateDate", new Date());
        }

    }

    public static String getParam(JSONObject param, String key, String defaultVal) throws Exception
    {
        if (!param.containsKey(key) || StringUtils.isEmpty(param.get(key))) return defaultVal;

        return param.getString(key);
    }

    public static String getParamWithCheck(JSONObject param, String key, boolean checknull, String defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getString(key);
    }

    public static int getParamWithCheck(JSONObject param, String key, boolean checknull, int defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getInteger(key);
    }

    public static long getParamWithCheck(JSONObject param, String key, boolean checknull, long defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getLongValue(key);
    }

    public static double getParamWithCheck(JSONObject param, String key, boolean checknull, double defaultVal) throws Exception
    {
        if (StringUtils.isEmpty(param) || !param.containsKey(key) || StringUtils.isEmpty(param.get(key)))
        {
            if (checknull)
            {
                throw new ServiceException(ResponseCode.FAILURE, "【{0}】 is Null", key);
            }
            else
            {
                return defaultVal;
            }
        }
        return param.getDoubleValue(key);
    }


}
