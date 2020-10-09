package com.efuture.wechat.db.model;

import groovy.transform.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @title: StaffCustQywxRelationModel
 * @description: 员工顾客企业微信关系表
 * @author: wangf
 * @date: 2020/07/14
 */
@Entity
@Table(name = "staff_cust_qywx_relation")
@ToString
public class StaffCustQywxRelationModel {

    public StaffCustQywxRelationModel(){};

    public StaffCustQywxRelationModel(Long ent_id, String id, String cust_qywxid){
        this.ent_id = ent_id;
        this.id = id;
        this.cust_qywxid = cust_qywxid;
    };

    public StaffCustQywxRelationModel(Long ph_key, Date ph_timestamp, Long ent_id, String id, String cid, String qywxid, String cust_cid, String cust_unionid, String cust_qywxid, String cust_avatar, Long tcrd, Long tmdd) {
        this.ph_key = ph_key;
        this.ph_timestamp = ph_timestamp;
        this.ent_id = ent_id;
        this.id = id;
        this.cid = cid;
        this.qywxid = qywxid;
        this.cust_cid = cust_cid;
        this.cust_unionid = cust_unionid;
        this.cust_qywxid = cust_qywxid;
        this.cust_avatar = cust_avatar;
        this.tcrd = tcrd;
        this.tmdd = tmdd;
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
    private String id;

    @Column
    private String cid;

    @Column
    private String qywxid;

    @Column
    private Integer cust_type; //客户类型 1-企业用户 2-微信用户

    @Column
    private String cust_cid;

    @Column
    private String cust_unionid;

    @Column
    private String cust_qywxid;

    @Column
    private String cust_avatar;

    @Column
    private Long tcrd;

    @Column
    private Long tmdd;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getQywxid() {
        return qywxid;
    }

    public void setQywxid(String qywxid) {
        this.qywxid = qywxid;
    }

    public Integer getCust_type() {
        return cust_type;
    }

    public void setCust_type(Integer cust_type) {
        this.cust_type = cust_type;
    }

    public String getCust_cid() {
        return cust_cid;
    }

    public void setCust_cid(String cust_cid) {
        this.cust_cid = cust_cid;
    }

    public String getCust_unionid() {
        return cust_unionid;
    }

    public void setCust_unionid(String cust_unionid) {
        this.cust_unionid = cust_unionid;
    }

    public String getCust_qywxid() {
        return cust_qywxid;
    }

    public void setCust_qywxid(String cust_qywxid) {
        this.cust_qywxid = cust_qywxid;
    }

    public String getCust_avatar() {
        return cust_avatar;
    }

    public void setCust_avatar(String cust_avatar) {
        this.cust_avatar = cust_avatar;
    }

    public Long getTcrd() {
        return tcrd;
    }

    public void setTcrd(Long tcrd) {
        this.tcrd = tcrd;
    }

    public Long getTmdd() {
        return tmdd;
    }

    public void setTmdd(Long tmdd) {
        this.tmdd = tmdd;
    }

    @Override
    public String toString() {
        return "StaffCustQywxRelationModel{" +
                "ph_key=" + ph_key +
                ", ph_timestamp=" + ph_timestamp +
                ", ent_id=" + ent_id +
                ", id='" + id + '\'' +
                ", cid='" + cid + '\'' +
                ", qywxid='" + qywxid + '\'' +
                ", cust_type='" + cust_type + '\'' +
                ", cust_cid='" + cust_cid + '\'' +
                ", cust_unionid='" + cust_unionid + '\'' +
                ", cust_qywxid='" + cust_qywxid + '\'' +
                ", cust_avatar='" + cust_avatar + '\'' +
                ", tcrd=" + tcrd +
                ", tmdd=" + tmdd +
                '}';
    }
}
