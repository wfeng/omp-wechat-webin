package com.efuture.wechat.serviceImpl;

import com.efuture.wechat.cp.config.WxCpConfiguration;
import com.efuture.wechat.db.model.CustIdentityModel;
import com.efuture.wechat.db.model.StaffCustQywxRelationModel;
import com.efuture.wechat.db.model.StaffinfoModel;
import com.efuture.wechat.db.repository.StaffCustQywxRelationRepository;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.service.CustIdentityService;
import com.efuture.wechat.service.StaffCustQywxRelationService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpExternalContactServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpUserExternalContactInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @title: StaffCustQywxRelationServiceImpl
 * @description: 员工关联顾客企业微信关系服务
 * @author: wangf
 * @date: 2020/07/15
 */
@Service
public class StaffCustQywxRelationServiceImpl implements StaffCustQywxRelationService
{
    @Value("${efuture.agentId}")
    String agentId;

    @Resource
    StaffCustQywxRelationRepository staffCustQywxRelationRepository;

    @Resource
    CustIdentityService custIdentityService;

    private static Logger logger = LoggerFactory.getLogger(StaffCustQywxRelationServiceImpl.class);

    /**
     * 同步员工客户企业微信关系数据
     */
    @Override
    public int syncStaffCustQywxRelation(StaffinfoModel staffinfo) {
        WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
        WxCpExternalContactServiceImpl wxCpExternalContactService = new WxCpExternalContactServiceImpl(wxCpService);
        int cnt = 0;
        try {
            List<String> externalContacts = wxCpExternalContactService.listExternalContacts(staffinfo.getQywxid());
            logger.info("员工[{}]{},查询到联系人数量:{}", staffinfo.getId(), staffinfo.getName(), externalContacts.size());
            List<StaffCustQywxRelationModel> existContacts = staffCustQywxRelationRepository.getExist(staffinfo.getEnt_id(), staffinfo.getCid(), externalContacts);
            //筛选出未登记的外部联系人列表
            for (StaffCustQywxRelationModel staffCustQywxRelation : existContacts) {
                externalContacts.remove(staffCustQywxRelation.getCust_qywxid());
            }
            logger.info("员工[{}]{},需要同步的联系人数量:{}", staffinfo.getId(), staffinfo.getName(), externalContacts.size());
            for (String code : externalContacts) {
                //不存在联系人关联关系才查询
//                if (!staffCustQywxRelationRepository.exists(Example.of(new StaffCustQywxRelationModel(staffinfo.getEnt_id(), staffinfo.getId(), code)))) {
                try {
                    WxCpUserExternalContactInfo detail = wxCpExternalContactService.getContactDetail(code);
                    //外部联系人类型 1- 表示该外部联系人是微信用户 2-表示该外部联系人是企业微信用户
                    if (detail.getExternalContact().getType() == 1) {
                        //unionId不等于空
                        if (StringUtils.isNotBlank(detail.getExternalContact().getUnionId())) {
                            //顾客头像
                            String avatar = detail.getExternalContact().getAvatar();
                            //顾客unionid
                            String unionId = detail.getExternalContact().getUnionId();
                            //顾客企业微信UserID
                            String externalUserId = detail.getExternalContact().getExternalUserId();

                            Optional<CustIdentityModel> custIdentityOptional = custIdentityService.getCustIdentity(staffinfo.getEnt_id(), unionId);
                            //是否存在顾客认证数据
                            if (custIdentityOptional.isPresent()) {
                                CustIdentityModel custIdentity = custIdentityOptional.get();

                                StaffCustQywxRelationModel staffCustQywxRelation = new StaffCustQywxRelationModel();
                                staffCustQywxRelation.setEnt_id(staffinfo.getEnt_id());
                                staffCustQywxRelation.setId(staffinfo.getId());
                                staffCustQywxRelation.setCust_unionid(unionId);

                                //查询员工顾客关系是否存在，不存在则增加
                                if (!staffCustQywxRelationRepository.exists(Example.of(staffCustQywxRelation))) {
                                    staffCustQywxRelation.setPh_timestamp(new Date());
                                    staffCustQywxRelation.setCid(staffinfo.getCid());
                                    staffCustQywxRelation.setQywxid(staffinfo.getQywxid());
                                    staffCustQywxRelation.setCust_type(detail.getExternalContact().getType());
                                    staffCustQywxRelation.setCust_cid(custIdentity.getCid());
                                    staffCustQywxRelation.setCust_avatar(avatar);
                                    staffCustQywxRelation.setCust_qywxid(externalUserId);
                                    staffCustQywxRelation.setTcrd(new Date().getTime() /1000);
                                    staffCustQywxRelation.setTmdd(new Date().getTime() /1000);
                                    //保存员工顾客关系数据
                                    staffCustQywxRelationRepository.save(staffCustQywxRelation);
                                    cnt++;
                                }
                            }
                        }
                    } else {
                        StaffCustQywxRelationModel staffCustQywxRelation = new StaffCustQywxRelationModel();
                        staffCustQywxRelation.setEnt_id(staffinfo.getEnt_id());
                        staffCustQywxRelation.setId(staffinfo.getId());
                        staffCustQywxRelation.setPh_timestamp(new Date());
                        staffCustQywxRelation.setCid(staffinfo.getCid());
                        staffCustQywxRelation.setQywxid(staffinfo.getQywxid());
                        staffCustQywxRelation.setCust_type(detail.getExternalContact().getType());
                        staffCustQywxRelation.setCust_cid(code);
                        staffCustQywxRelation.setCust_qywxid(code);
                        staffCustQywxRelation.setTcrd(new Date().getTime() /1000);
                        staffCustQywxRelation.setTmdd(new Date().getTime() /1000);
                        //保存员工顾客关系数据
                        staffCustQywxRelationRepository.save(staffCustQywxRelation);
                        cnt++;
                    }
                    //程序睡眠的目的是为了防止请求量太大导致企业微信服务拒绝访问
                    Thread.sleep(200);
                } catch (WxErrorException | InterruptedException e) {
                    logger.error("同步员工顾客企业微信数据错误", e);
                } catch (RuntimeException ex) {
                    logger.error("同步员工顾客企业微信数据错误", ex);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        logger.error("同步员工顾客企业微信数据错误", ex);
                    }
                }
//                }
            }
        } catch (WxErrorException e) {
            logger.error("同步员工顾客企业微信数据错误", e);
        } catch (RuntimeException ex) {
            logger.error("同步员工顾客企业微信数据错误", ex);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("同步员工顾客企业微信数据错误", ex);
            }
        }
        return cnt;
    }

    @Override
    public List<StaffCustQywxRelationModel> getCustList(ServiceSession session, List<String> cids) {
        return staffCustQywxRelationRepository.getCustRelationListByCustCid(session.getEnt_id(), cids);
    }
}
