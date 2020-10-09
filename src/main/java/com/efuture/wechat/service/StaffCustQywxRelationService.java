package com.efuture.wechat.service;

import com.efuture.wechat.db.model.StaffCustQywxRelationModel;
import com.efuture.wechat.db.model.StaffinfoModel;
import com.efuture.wechat.entity.ServiceSession;

import java.util.List;

/**
 * @title: StaffCustQywxRelationService
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/15
 */

public interface StaffCustQywxRelationService {
    int syncStaffCustQywxRelation(StaffinfoModel staffinfo);
    List<StaffCustQywxRelationModel> getCustList(ServiceSession session, List<String> cids);
}
