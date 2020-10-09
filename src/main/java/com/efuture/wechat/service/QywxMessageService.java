package com.efuture.wechat.service;

import com.efuture.wechat.db.model.QywxMessageLogModel;
import com.efuture.wechat.entity.ServiceSession;

import java.util.List;

/**
 * @title: QywxMessageService
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/17
 */

public interface QywxMessageService {
    boolean existTransId(ServiceSession session, String TransId);

    QywxMessageLogModel buildMsg(Long ent_id, String trans_id, String msg_id, String sender_id, String external_id, String msg);

    QywxMessageLogModel saveMsgLog(QywxMessageLogModel model);

    List<QywxMessageLogModel> saveBatchMsgLog(List<QywxMessageLogModel> logs);

    List<String> getMsgIdsByStatus(ServiceSession session, String status);
}
