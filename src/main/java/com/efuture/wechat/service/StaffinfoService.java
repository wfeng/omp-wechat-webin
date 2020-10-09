package com.efuture.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.db.model.StaffinfoModel;

import java.util.List;

/**
 * @title: StaffinfoService
 * @description: TODO
 * @author: wangf
 * @date: 2020/07/15
 */

public interface StaffinfoService {

    JSONObject queryStaffList(JSONObject jsonparam);

    public List<StaffinfoModel> getStaffListByCids(List<String> cids);
}
