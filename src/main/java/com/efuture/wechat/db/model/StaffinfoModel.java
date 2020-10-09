package com.efuture.wechat.db.model;

import groovy.transform.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @title: StaffinfoModel
 * @description: 员工信息表
 * @author: wangf
 * @date: 2020/07/14
 */

@Entity
@Table(name = "staffinfo")
@ToString
public class StaffinfoModel {

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
    private String id; //员工ID

    @Column
    private String name; //员工姓名

    @Column
    private String mobile; //员工电话

    @Column
    private String cid; //员工CID

    @Column
    private Date hiredate; //入职日期

    @Column
    private String type; //类型 1专柜员工  2自营员工

    @Column
    private String status; //状态 Y和N  启用/禁用

    @Column
    private String corpid; //经营公司

    @Column
    private String mktid; //门店编号

    @Column
    private String mktname; //门店

    @Column
    private String floorid; //楼层编号

    @Column
    private String floorname; //楼层

    @Column
    private String regionid; //区域编号

    @Column
    private String regionname; //区域

    @Column
    private String mfid; //柜组编码

    @Column
    private String mfname; //柜组名称

    @Column
    private String memo; //备注

    @Column
    private Long tcrd; //创建时间戳

    @Column
    private Long tmdd; //更新时间戳

    @Column
    private String qywxid; //员工企业微信号

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getMktid() {
        return mktid;
    }

    public void setMktid(String mktid) {
        this.mktid = mktid;
    }

    public String getMktname() {
        return mktname;
    }

    public void setMktname(String mktname) {
        this.mktname = mktname;
    }

    public String getFloorid() {
        return floorid;
    }

    public void setFloorid(String floorid) {
        this.floorid = floorid;
    }

    public String getFloorname() {
        return floorname;
    }

    public void setFloorname(String floorname) {
        this.floorname = floorname;
    }

    public String getRegionid() {
        return regionid;
    }

    public void setRegionid(String regionid) {
        this.regionid = regionid;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getMfid() {
        return mfid;
    }

    public void setMfid(String mfid) {
        this.mfid = mfid;
    }

    public String getMfname() {
        return mfname;
    }

    public void setMfname(String mfname) {
        this.mfname = mfname;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getQywxid() {
        return qywxid;
    }

    public void setQywxid(String qywxid) {
        this.qywxid = qywxid;
    }

    @Override
    public String toString() {
        return "StaffinfoModel{" +
                "ph_key=" + ph_key +
                ", ph_timestamp=" + ph_timestamp +
                ", ent_id=" + ent_id +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cid='" + cid + '\'' +
                ", hiredate=" + hiredate +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", corpid='" + corpid + '\'' +
                ", mktid='" + mktid + '\'' +
                ", mktname='" + mktname + '\'' +
                ", floorid='" + floorid + '\'' +
                ", floorname='" + floorname + '\'' +
                ", regionid='" + regionid + '\'' +
                ", regionname='" + regionname + '\'' +
                ", mfid='" + mfid + '\'' +
                ", mfname='" + mfname + '\'' +
                ", memo='" + memo + '\'' +
                ", tcrd=" + tcrd +
                ", tmdd=" + tmdd +
                ", qywxid='" + qywxid + '\'' +
                '}';
    }
}
