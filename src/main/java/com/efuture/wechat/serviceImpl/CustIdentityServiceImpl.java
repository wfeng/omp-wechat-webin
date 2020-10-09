package com.efuture.wechat.serviceImpl;

import com.efuture.wechat.constant.OcmInfoCode;
import com.efuture.wechat.db.model.CustIdentityModel;
import com.efuture.wechat.db.repository.CustIdentityRepository;
import com.efuture.wechat.service.CustIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @title: CustIdentityServiceImpl
 * @description: 会员认证服务
 * @author: wangf
 * @date: 2020/07/15
 */
@Service
public class CustIdentityServiceImpl implements CustIdentityService {

    @Resource
    CustIdentityRepository custIdentityRepository;

    @Override
    public Optional<CustIdentityModel> getCustIdentity(long ent_id, String unionId) {
        CustIdentityModel custIdentityWhere = new CustIdentityModel();
        custIdentityWhere.setEnt_id(ent_id);
        custIdentityWhere.setIdtype(OcmInfoCode.IdMethod.WxUnionID);
        custIdentityWhere.setIdcode(unionId);
        custIdentityWhere.setNsta(OcmInfoCode.Nsta.Normal);
        Example<CustIdentityModel> query = Example.of(custIdentityWhere);
        return custIdentityRepository.findOne(query);
    }
}
