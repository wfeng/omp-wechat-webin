package com.efuture.wechat.db.repository;

import com.efuture.wechat.db.model.StaffCustQywxRelationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @title: StaffCustQywxRelationRepository
 * @description: 员工顾客企业微信关系对象
 * @author: wangf
 * @date: 2020/07/14
 */

public interface StaffCustQywxRelationRepository extends JpaRepository<StaffCustQywxRelationModel, Long> {
    @Query(value = "select * from staff_cust_qywx_relation where ent_id = :ent_id and cid = :cid and cust_qywxid in :cust_qywxids", nativeQuery = true)
    List<StaffCustQywxRelationModel> getExist(@Param("ent_id") long ent_id, @Param("cid") String cid, @Param("cust_qywxids")List<String> cust_qywxids);

    @Query(value = "select * from staff_cust_qywx_relation where ent_id = :ent_id and cust_cid in :cust_cids", nativeQuery = true)
    List<StaffCustQywxRelationModel> getCustRelationListByCustCid(@Param("ent_id") long ent_id, @Param("cust_cids") List<String> cust_cids);
}
