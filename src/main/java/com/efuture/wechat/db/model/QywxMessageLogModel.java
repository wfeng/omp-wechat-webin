package com.efuture.wechat.db.model;

import groovy.transform.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @title: QywxMessageLogModel
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/17
 */
@Entity
@Table(name = "qywx_message_log")
@ToString
public class QywxMessageLogModel {

    public QywxMessageLogModel(){}

    public QywxMessageLogModel(Long ph_key, Date ph_timestamp, Long ent_id, String trans_id, String msg_id, String sender_id, String external_id, String msg, String status) {
        this.ph_key = ph_key;
        this.ph_timestamp = ph_timestamp;
        this.ent_id = ent_id;
        this.trans_id = trans_id;
        this.msg_id = msg_id;
        this.sender_id = sender_id;
        this.external_id = external_id;
        this.msg = msg;
        this.status = status;
    }

    @Id
    @GeneratedValue(generator = "generate",
            strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "generate",
            strategy = "com.efuture.wechat.config.PhKeyGeneratorConfig")
    private Long ph_key; //主键

    @Column
    private Date ph_timestamp; //时间戳

    @Column
    private Long ent_id; //企业ID

    @Column
    private String trans_id;

    @Column
    private String msg_id;

    @Column
    private String sender_id;

    @Column
    private String external_id;

    @Column
    private String msg;

    @Column
    private String status;

    public Long getPh_key() {
        return ph_key;
    }

    public void setPh_key(Long ph_key) {
        this.ph_key = ph_key;
    }

    public Date getPh_timestamp() {
        return ph_timestamp;
    }

    public void setPh_timestamp(Date ph_timestamp) {
        this.ph_timestamp = ph_timestamp;
    }

    public Long getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(Long ent_id) {
        this.ent_id = ent_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QywxMessageLogModel{" +
                "ph_key=" + ph_key +
                ", ph_timestamp=" + ph_timestamp +
                ", ent_id=" + ent_id +
                ", trans_id='" + trans_id + '\'' +
                ", msg_id='" + msg_id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", external_id='" + external_id + '\'' +
                ", msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
