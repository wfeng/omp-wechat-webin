package com.efuture.wechat.cp.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.cp.service.WxCpMsgService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @title: WxCpMsgServiceImpl
 * @description: 扩展企业微信消息接口调用
 * @author: wangf
 * @date: 2020/07/09
 */

public class WxCpMsgServiceImpl implements WxCpMsgService
{
    public static final String ADD_MSG_URL = "/cgi-bin/externalcontact/add_msg_template";
    public static final String GET_MSG_RESULT_URL = "/cgi-bin/externalcontact/get_group_msg_result";

    private final WxCpService cpService;

    public WxCpMsgServiceImpl(WxCpService cpService) {
        this.cpService = cpService;
    }

    @Override
    public JSONObject addMsg(List<String> externalUserid, String sender, String textContent, JSONObject image, JSONObject link, JSONObject miniprogram) throws WxErrorException {
        return addMsg("single", externalUserid, sender, textContent, image, link, miniprogram);
    }

    @Override
    public JSONObject addMsg(String chat_type, List<String> externalUserid, String sender, String textContent, JSONObject image, JSONObject link, JSONObject miniprogram) throws WxErrorException {
        JSONObject json = new JSONObject();
        if (StringUtils.isNotBlank(chat_type)) {
            json.put("chat_type", chat_type);
        } else {
            json.put("chat_type", "single");
        }
        if (externalUserid != null && !externalUserid.isEmpty()) {
            json.put("external_userid", externalUserid);
        }
        if (StringUtils.isNotBlank(sender)) {
            json.put("sender", sender);
        }
        if (StringUtils.isNotBlank(textContent)) {
            JSONObject textJson = new JSONObject();
            textJson.put("content", textContent);
            json.put("text", textJson);
        }
        if (image != null && !image.isEmpty()) {
            json.put("image", image);
        }
        if (link != null && !link.isEmpty()) {
            json.put("link", link);
        }
        if (miniprogram != null && !miniprogram.isEmpty()) {
            json.put("miniprogram", miniprogram);
        }

        final String url = this.cpService.getWxCpConfigStorage().getApiUrl(ADD_MSG_URL);
        String responseContent = this.cpService.post(url, json.toJSONString());

        return JSONObject.parseObject(responseContent);
    }

    @Override
    public JSONObject getMsgResult(String msgid) throws WxErrorException {
        JSONObject json = new JSONObject();
        json.put("msgid", msgid);
        final String url = this.cpService.getWxCpConfigStorage().getApiUrl(GET_MSG_RESULT_URL);
        String responseContent = this.cpService.post(url, json.toJSONString());

        return JSONObject.parseObject(responseContent);
    }
}
