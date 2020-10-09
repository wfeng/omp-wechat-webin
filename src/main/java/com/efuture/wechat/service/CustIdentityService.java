package com.efuture.wechat.service;

import com.efuture.wechat.db.model.CustIdentityModel;

import java.util.Optional;

/**
 * @title: CustIdentityService
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/15
 */

public interface CustIdentityService {

    Optional<CustIdentityModel> getCustIdentity(long ent_id, String unionId);
}
