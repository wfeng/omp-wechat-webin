package com.efuture.wechat.intf;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.entity.ServiceResponse;
import com.efuture.wechat.entity.ServiceSession;

/**
 * @title: ICpWechatMessage
 * @description: 企业微信消息接口
 * @author: wangf
 * @date: 2020/07/16
 */

public interface ICpWechatMessage {
    ServiceResponse send(ServiceSession session, JSONObject jsonparam) throws Exception;
}
