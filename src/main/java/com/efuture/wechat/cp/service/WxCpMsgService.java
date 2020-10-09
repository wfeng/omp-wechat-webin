package com.efuture.wechat.cp.service;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * @title: WxCpMsgService
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/09
 */

public interface WxCpMsgService {
    JSONObject addMsg(List<String> externalUserid, String sender, String textContent, JSONObject image, JSONObject link, JSONObject miniprogram) throws WxErrorException;
    JSONObject addMsg(String chat_type, List<String> externalUserid, String sender, String textContent, JSONObject image, JSONObject link, JSONObject miniprogram) throws WxErrorException;
    JSONObject getMsgResult(String msgid) throws WxErrorException;
}
