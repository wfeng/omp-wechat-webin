package com.efuture.wechat.db.repository;

import com.efuture.wechat.db.model.CustIdentityModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @title: CustIdentityRepository
 * @description: 顾客认证对象
 * @author: wangf
 * @date: 2020/07/14
 */

public interface CustIdentityRepository extends JpaRepository<CustIdentityModel, Long> {
}
