package com.efuture.wechat.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.db.model.StaffinfoModel;
import com.efuture.wechat.db.repository.StaffinfoRepository;
import com.efuture.wechat.service.StaffinfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @title: StaffinfoServiceImpl
 * @description: 员工信息服务
 * @author: wangf
 * @date: 2020/07/15
 */
@Service
public class StaffinfoServiceImpl implements StaffinfoService {

    @Resource
    StaffinfoRepository staffinfoRepository;

    /**
     * 读取有效员工数据
     * @param jsonparam
     * @return
     */
    @Override
    public JSONObject queryStaffList(JSONObject jsonparam) {
        int page_no = 1, page_size = 50;
        if (jsonparam.containsKey("page_no")) {
            page_no = jsonparam.getInteger("page_no");
        }
        if (jsonparam.containsKey("page_size")) {
            page_size = jsonparam.getInteger("page_size");
        }
        //查询有效员工数据
        Pageable pageable = PageRequest.of(page_no - 1, page_size);
        Page<StaffinfoModel> list = staffinfoRepository.findExistQywxStaffinfo(pageable);
        JSONObject result = new JSONObject();
        result.put("total_pages", list.getTotalPages());
        result.put("total_results", list.getTotalElements());
        result.put("list", list.getContent());
        return result;
    }

    @Override
    public List<StaffinfoModel> getStaffListByCids(List<String> cids) {
        return staffinfoRepository.getStaffinfoByCids(cids);
    }
}
