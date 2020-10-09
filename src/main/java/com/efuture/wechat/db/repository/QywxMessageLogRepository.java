package com.efuture.wechat.db.repository;

import com.efuture.wechat.db.model.QywxMessageLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @title: QywxMessageLogRepository
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/17
 */

public interface QywxMessageLogRepository extends JpaRepository<QywxMessageLogModel, Long> {

    @Query(value = "select count(*) from qywx_message_log where ent_id = :ent_id and trans_id = :trans_id", nativeQuery = true)
    Long existTransId(@Param("ent_id") long ent_id, @Param("trans_id") String trans_id);

    @Query(value = "select distinct msg_id from qywx_message_log where ent_id = :ent_id and status = :status", nativeQuery = true)
    List<String> getMsgIdByStatus(@Param("ent_id") long ent_id, @Param("status") String status);
}
