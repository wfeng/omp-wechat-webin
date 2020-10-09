package com.efuture.wechat.db.model;

import groovy.transform.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @title: CustIdentityModel
 * @description: 顾客认证表
 * @author: wangf
 * @date: 2020/07/14
 */
@Entity
@Table(name = "custidentity")
@ToString
public class CustIdentityModel {

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
    private Long ent_id;

    @Column
    private String cid;

    @Column
    private String idtype;

    @Column
    private String idcode;

//    @Column
//    private String channel;
//
//    @Column
//    private String regsrc;

    @Column
    private Date dstart;

    @Column
    private Date dend;

    @Column
    private String cnote;

    @Column
    private Long tcrd;

    @Column
    private Long tmdd;

    @Column
    private Integer nsta;

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getIdcode() {
        return idcode;
    }

    public void setIdcode(String idcode) {
        this.idcode = idcode;
    }

//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }
//
//    public String getRegsrc() {
//        return regsrc;
//    }
//
//    public void setRegsrc(String regsrc) {
//        this.regsrc = regsrc;
//    }

    public Date getDstart() {
        return dstart;
    }

    public void setDstart(Date dstart) {
        this.dstart = dstart;
    }

    public Date getDend() {
        return dend;
    }

    public void setDend(Date dend) {
        this.dend = dend;
    }

    public String getCnote() {
        return cnote;
    }

    public void setCnote(String cnote) {
        this.cnote = cnote;
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

    public Integer getNsta() {
        return nsta;
    }

    public void setNsta(Integer nsta) {
        this.nsta = nsta;
    }

    @Override
    public String toString() {
        return "CustIdentityModel{" +
                "ph_key=" + ph_key +
                ", ph_timestamp=" + ph_timestamp +
                ", ent_id=" + ent_id +
                ", cid='" + cid + '\'' +
                ", idtype='" + idtype + '\'' +
                ", idcode='" + idcode + '\'' +
//                ", channel='" + channel + '\'' +
//                ", regsrc='" + regsrc + '\'' +
                ", dstart=" + dstart +
                ", dend=" + dend +
                ", cnote='" + cnote + '\'' +
                ", tcrd=" + tcrd +
                ", tmdd=" + tmdd +
                ", nsta=" + nsta +
                '}';
    }
}
