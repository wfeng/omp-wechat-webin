package com.efuture.wechat.serviceImpl;

import com.efuture.wechat.db.model.QywxMessageLogModel;
import com.efuture.wechat.db.repository.QywxMessageLogRepository;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.service.QywxMessageService;
import com.github.binarywang.java.emoji.EmojiConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @title: QywxMessageServiceImpl
 * @description: 企业微信消息服务
 * @author: wangf
 * @date: 2020/07/17
 */
@Service
public class QywxMessageServiceImpl implements QywxMessageService {
    private EmojiConverter emojiConverter = EmojiConverter.getInstance();
    @Autowired
    QywxMessageLogRepository qywxMessageLogRepository;
    @Override
    public boolean existTransId(ServiceSession session, String trans_id) {
        long cnt = qywxMessageLogRepository.existTransId(session.getEnt_id(), trans_id);
        return cnt > 0;
    }

    @Override
    public QywxMessageLogModel buildMsg(Long ent_id, String trans_id, String msg_id, String sender_id, String external_id, String msg) {
        msg = emojiConverter.toHtml(msg);
        QywxMessageLogModel model = new QywxMessageLogModel();
        model.setEnt_id(ent_id);
        model.setTrans_id(trans_id);
        model.setMsg_id(msg_id);
        model.setSender_id(sender_id);
        model.setExternal_id(external_id);
        model.setMsg(msg);
        model.setStatus("N");
        model.setPh_timestamp(new Date());
        return model;
    }

    @Override
    public QywxMessageLogModel saveMsgLog(QywxMessageLogModel model) {
        return qywxMessageLogRepository.save(model);
    }

    @Override
    public List<QywxMessageLogModel> saveBatchMsgLog(List<QywxMessageLogModel> logs) {
        return qywxMessageLogRepository.saveAll(logs);
    }

    @Override
    public List<String> getMsgIdsByStatus(ServiceSession session, String status) {
        List<String> msgIds = qywxMessageLogRepository.getMsgIdByStatus(session.getEnt_id(), status);
        return msgIds;
    }
}
