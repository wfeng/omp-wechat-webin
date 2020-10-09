package com.efuture.wechat.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.common.ServiceLogs;
import com.efuture.wechat.config.RedisConfig;
import com.efuture.wechat.constant.ResponseCode;
import com.efuture.wechat.cp.config.WxCpConfiguration;
import com.efuture.wechat.cp.serviceImpl.WxCpMsgServiceImpl;
import com.efuture.wechat.db.model.QywxMessageLogModel;
import com.efuture.wechat.db.model.StaffCustQywxRelationModel;
import com.efuture.wechat.entity.ServiceResponse;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.intf.ICpWechatMessage;
import com.efuture.wechat.service.QywxMessageService;
import com.efuture.wechat.service.StaffCustQywxRelationService;
import com.efuture.wechat.utils.CommonUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @title: ICpWechatMessageImpl
 * @description: 企业微信消息接口实现类
 * @author: wangf
 * @date: 2020/07/16
 */
@Component("efuture.wechat.cp.message")
public class ICpWechatMessageImpl implements ICpWechatMessage {

    //@TODO 当前版本为兰州国芳版本，私有化项目，不需配置企业信息，读取配置从配置文件中读取
    @Value("${efuture.agentId}")
    String agentId;

    @Autowired
    StaffCustQywxRelationService staffCustQywxRelationService;

    @Autowired
    QywxMessageService qywxMessageService;

    @Autowired
    private RedisConfig redisConfig;

    @Override
    public ServiceResponse send(ServiceSession session, JSONObject jsonparam) {

        String trans_id = null;
        JSONArray cidArray = new JSONArray();
        String text = null;
        JSONObject link = null;
        if (jsonparam.containsKey("trans_id")) {
            trans_id = jsonparam.getString("trans_id");
        }
        if (jsonparam.containsKey("cids") && jsonparam.get("cids") instanceof JSONArray) {
            cidArray = jsonparam.getJSONArray("cids");
        }
        if (jsonparam.containsKey("text")) {
            text = jsonparam.getString("text");
        }
        if (jsonparam.containsKey("link")) {
            link = jsonparam.getJSONObject("link");
        }

        if (StringUtils.isEmpty(trans_id)) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "trans_id is null");
        }

        if (cidArray.isEmpty()) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "cids is null");
        }

        if (text == null && link == null) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "text/link is null");
        }

        if (qywxMessageService.existTransId(session, trans_id)) {
            return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "trans_id重复");
        }

        String key = "CpWechatMessageServiceImpl-" + session.getEnt_id() + "-" + trans_id;
        try{
            Jedis jedis = redisConfig.redisPoolFactory().getResource();
            if (jedis.exists(key)) {
                return ServiceResponse.buildFailure(session, ResponseCode.FAILURE, "trans_id重复");
            } else {
                jedis.setex(key, 60, "x");
            }
        } catch (Exception e) {
            ServiceLogs.debuglog("jedis_err", e.getMessage(), 0);
        }

        List<String> cids = cidArray.toJavaList(String.class);
        Map<String, List<String>> staffGroup = new HashMap<>();
        List<StaffCustQywxRelationModel> custList = staffCustQywxRelationService.getCustList(session, cids);
        for (StaffCustQywxRelationModel staffCust: custList) {
            for (String cid : cids) {
                if (staffCust.getCust_cid().equals(cid)) {
                    List<String> list;
                    if (!staffGroup.containsKey(staffCust.getQywxid())) {
                        list = new ArrayList<>();
                        staffGroup.put(staffCust.getQywxid(), list);
                    }
                    list = staffGroup.get(staffCust.getQywxid());
                    list.add(staffCust.getCust_qywxid());
                    staffGroup.put(staffCust.getQywxid(), list);
                    cids.remove(cid);
                    break;
                }
            }
        }
        WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
        WxCpMsgServiceImpl wxCpMsgService = new WxCpMsgServiceImpl(wxCpService);
        Iterator staffQywxids = staffGroup.keySet().iterator();
        while (staffQywxids.hasNext()) {
            String staffQywxid = (String) staffQywxids.next();
            List<String> externalUserid = staffGroup.get(staffQywxid);
            String msgId = null;
            Integer errcode = null;
            String errmsg = null;
            try {
                JSONObject msgJson = new JSONObject();
                msgJson.put("text", text);
                msgJson.put("link", link);
                JSONObject result = wxCpMsgService.addMsg(externalUserid, staffQywxid, text, null, link, null);
                errcode = CommonUtils.getParamWithCheck(result, "errcode", false,1);
                if (errcode == 0) {
                    msgId = CommonUtils.getParamWithCheck(result, "msgid", false,null);
                } else {
                    errmsg = CommonUtils.getParamWithCheck(result, "errmsg", false,null);
                }
                //发送失败，重发一次
                if (errcode != 0) {
                    result = wxCpMsgService.addMsg(externalUserid, staffQywxid, text, null, link, null);
                }
                errcode = CommonUtils.getParamWithCheck(result, "errcode", false,1);
                if (errcode == 0) {
                    msgId = CommonUtils.getParamWithCheck(result, "msgid", false,null);
                } else {
                    errmsg = CommonUtils.getParamWithCheck(result, "errmsg", false,null);
                }

                if (errcode == 0) {
                    List<QywxMessageLogModel> logs = new ArrayList<>();
                    for (String extUserId : externalUserid) {
                        QywxMessageLogModel model = qywxMessageService.buildMsg(session.getEnt_id(), trans_id, msgId, staffQywxid, extUserId, msgJson.toJSONString());
                        logs.add(model);
//                        qywxMessageService.saveMsgLog(model);
                        //改为批量保存
                        if (logs.size() % 1000 == 0) {
                            qywxMessageService.saveBatchMsgLog(logs);
                            logs = new ArrayList<>();
                        }
                    }
                    if (!logs.isEmpty()) {
                        qywxMessageService.saveBatchMsgLog(logs);
                    }
                }
                ServiceLogs.debuglog("send_result", result.toJSONString(), 0);
            } catch (WxErrorException e) {
                ServiceLogs.debuglog("send_error", e.getMessage(), 0);
            } catch (Exception e) {
                ServiceLogs.debuglog("send_error", e.getMessage(), 0);
            }
        }
        JSONObject data = new JSONObject();
        data.put("trans_id", trans_id);
        data.put("staffGroup", staffGroup);
        return ServiceResponse.buildSuccess(data);
    }
}
