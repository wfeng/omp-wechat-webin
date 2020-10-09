package com.efuture.wechat.db.repository;

import com.efuture.wechat.db.model.StaffinfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @title: StaffinfoRepository
 * @description: 员工信息对象
 * @author: wangf
 * @date: 2020/07/14
 */

public interface StaffinfoRepository extends JpaRepository<StaffinfoModel, Long> {

//    //Oracle版本
//    @Query(value = "SELECT * FROM STAFFINFO WHERE STATUS = 'Y' and qywxid is not null ORDER BY /*#pageable*/",
//            countQuery = "SELECT COUNT(*) FROM STAFFINFO WHERE STATUS = 'Y' AND QYWXID IS NOT NULL",
//            nativeQuery = true)
//    Page<StaffinfoModel> findExistQywxStaffinfo(Pageable pageable);

    @Query(value = "SELECT * FROM STAFFINFO WHERE STATUS = 'Y' and qywxid is not null",
            countQuery = "SELECT COUNT(*) FROM STAFFINFO WHERE STATUS = 'Y' AND QYWXID IS NOT NULL",
            nativeQuery = true)
    Page<StaffinfoModel> findExistQywxStaffinfo(Pageable pageable);


    @Query(value = "select * from STAFFINFO where STATUS = 'Y' and cid in :cids", nativeQuery = true)
    List<StaffinfoModel> getStaffinfoByCids(@Param("cids") List<String> cids);

}
