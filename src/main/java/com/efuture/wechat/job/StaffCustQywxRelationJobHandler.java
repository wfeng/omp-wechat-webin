package com.efuture.wechat.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.db.model.StaffinfoModel;
import com.efuture.wechat.service.StaffCustQywxRelationService;
import com.efuture.wechat.service.StaffinfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @title: StaffCustQywxRelationJobHandler
 * @description: 员工客户企业微信关系任务
 * @author: wangf
 * @date: 2020/07/13
 */
@Component
@JobHandler(value="StaffCustQywxRelationJobHandler")
public class StaffCustQywxRelationJobHandler extends IJobHandler {

    @Value("${efuture.agentId}")
    String agentId;

    @Resource
    StaffCustQywxRelationService staffCustQywxRelationService;

    @Resource
    StaffinfoService staffinfoService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("开始同步员工客户企业微信关系数据");
        XxlJobLogger.log("企业配置:{}", agentId);

        if (!StringUtils.isEmpty(param)) {
            String[] cids = param.split(",");
            XxlJobLogger.log("指定同步员工编号:{}", JSONObject.toJSONString(cids));
            List<String> staffList = new ArrayList<>();
            Collections.addAll(staffList, cids);
            List<StaffinfoModel> staffinfoList = staffinfoService.getStaffListByCids(staffList);
            XxlJobLogger.log("查询到{}个员工数量", staffinfoList.size());
            for (StaffinfoModel staffinfo : staffinfoList) {
                XxlJobLogger.log("同步员工:{}的顾客关系", staffinfo.getName());
                int cnt = staffCustQywxRelationService.syncStaffCustQywxRelation(staffinfo);
                XxlJobLogger.log("完成员工:{}的顾客关系同步数量:{}", staffinfo.getName(), cnt);
            }
            XxlJobLogger.log("同步指定员工客户企业微信关系数据完成");
            return ReturnT.SUCCESS;
        }

        int page_no = 1, page_size = 10;
        JSONObject jsonparam = new JSONObject();
        jsonparam.put("page_no", page_no);
        jsonparam.put("page_size", page_size);
        JSONObject result = staffinfoService.queryStaffList(jsonparam);
        Integer total_pages = result.getInteger("total_pages");
        Integer total_results = result.getInteger("total_results");
        JSONArray list = result.getJSONArray("list");
        XxlJobLogger.log("查询到{}个员工信息", total_results);
        XxlJobLogger.log("总分页数：{}", total_pages);
        for (int i = 0; i < list.size(); i++) {
            StaffinfoModel staffinfo = JSONObject.parseObject(list.getJSONObject(i).toJSONString(), StaffinfoModel.class);
            XxlJobLogger.log("同步员工:{}的顾客关系", staffinfo.getName());
            int cnt = staffCustQywxRelationService.syncStaffCustQywxRelation(staffinfo);
            XxlJobLogger.log("完成员工:{}的顾客关系同步数量:{}", staffinfo.getName(), cnt);
        }
        page_no++;
        for (; page_no <= total_pages; page_no++) {
            XxlJobLogger.log("查询第{}页数据", page_no);
            jsonparam.put("page_no", page_no);
            result = staffinfoService.queryStaffList(jsonparam);
            list = result.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                StaffinfoModel staffinfo = JSONObject.parseObject(list.getJSONObject(i).toJSONString(), StaffinfoModel.class);
                XxlJobLogger.log("同步员工:{}的顾客关系", staffinfo.getName());
                int cnt = staffCustQywxRelationService.syncStaffCustQywxRelation(staffinfo);
                XxlJobLogger.log("完成员工:{}的顾客关系同步数量:{}", staffinfo.getName(), cnt);
            }
        }
        XxlJobLogger.log("同步员工客户企业微信关系数据完成");
        return ReturnT.SUCCESS;
    }
}
