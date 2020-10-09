package com.efuture.wechat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.efuture.wechat.constant.OcmInfoCode;
import com.efuture.wechat.cp.config.WxCpConfiguration;
import com.efuture.wechat.cp.serviceImpl.WxCpMsgServiceImpl;
import com.efuture.wechat.db.model.CustIdentityModel;
import com.efuture.wechat.db.model.StaffCustQywxRelationModel;
import com.efuture.wechat.db.model.StaffinfoModel;
import com.efuture.wechat.db.repository.CustIdentityRepository;
import com.efuture.wechat.db.repository.StaffCustQywxRelationRepository;
import com.efuture.wechat.db.repository.StaffinfoRepository;
import com.efuture.wechat.entity.ServiceSession;
import com.efuture.wechat.service.QywxMessageService;
import com.efuture.wechat.service.StaffCustQywxRelationService;
import com.efuture.wechat.service.StaffinfoService;
import com.xxl.job.core.log.XxlJobLogger;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpChatServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpExternalContactServiceImpl;
import me.chanjar.weixin.cp.api.impl.WxCpUserServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.WxCpUserExternalContactInfo;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@SpringBootTest
@WebAppConfiguration
class DemoApplicationTests {

        private String userId = "Test";
    private String key1 = 0 + "_" + 1000006;
    private String key2 = 1 + "_" + 1000006;


    @Autowired
    StaffinfoRepository staffinfoRepository;

    @Autowired
    CustIdentityRepository custIdentityRepository;

    @Autowired
    StaffCustQywxRelationRepository staffCustQywxRelationRepository;

    @Autowired
    StaffinfoService staffinfoService;

    @Autowired
    StaffCustQywxRelationService staffCustQywxRelationService;

    @Autowired
    QywxMessageService qywxMessageService;

    private static Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

    @Test
    void contextLoads() {
    }

    private Optional<CustIdentityModel> getCustIdentity(long ent_id, String unionId) {
        CustIdentityModel custIdentityWhere = new CustIdentityModel();
        custIdentityWhere.setEnt_id(ent_id);
        custIdentityWhere.setIdtype(OcmInfoCode.IdMethod.WxUnionID);
        custIdentityWhere.setIdcode(unionId);
        custIdentityWhere.setNsta(OcmInfoCode.Nsta.Normal);
        Example<CustIdentityModel> query = Example.of(custIdentityWhere);
        return custIdentityRepository.findOne(query);
    }

    private void syncStaffCustQywxRelation(StaffinfoModel staffinfo) {
        WxCpService wxCpService = WxCpConfiguration.getCpService(key2);
        WxCpExternalContactServiceImpl wxCpExternalContactService = new WxCpExternalContactServiceImpl(wxCpService);
        try {
            List<String> externalContacts = wxCpExternalContactService.listExternalContacts(staffinfo.getQywxid());
            for (String code : externalContacts) {
                //不存在联系人关联关系才查询
                if (!staffCustQywxRelationRepository.exists(Example.of(new StaffCustQywxRelationModel(staffinfo.getEnt_id(), staffinfo.getId(), code)))) {
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

                            Optional<CustIdentityModel> custIdentityOptional = getCustIdentity(staffinfo.getEnt_id(), unionId);
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
                                    staffCustQywxRelation.setCust_cid(custIdentity.getCid());
                                    staffCustQywxRelation.setCust_avatar(avatar);
                                    staffCustQywxRelation.setCust_qywxid(externalUserId);
                                    staffCustQywxRelation.setTcrd(new Date().getTime() /1000);
                                    staffCustQywxRelation.setTmdd(new Date().getTime() /1000);
                                    //保存员工顾客关系数据
                                    staffCustQywxRelationRepository.save(staffCustQywxRelation);
                                }
                            }
                        }
                    }
                }
            }
        } catch (WxErrorException e) {
            XxlJobLogger.log(e);
        }
    }

    private JSONObject queryStaffList(JSONObject jsonparam) {
        int page_no = 1, page_size = 50;
        if (jsonparam.containsKey("page_no")) {
            page_no = jsonparam.getInteger("page_no");
        }
        if (jsonparam.containsKey("page_size")) {
            page_size = jsonparam.getInteger("page_size");
        }
        //查询有效员工数据
//        StaffinfoModel staffinfoWhere = new StaffinfoModel();
//        staffinfoWhere.setStatus("Y");
//        Example<StaffinfoModel> query = Example.of(staffinfoWhere);
        Pageable pageable = PageRequest.of(page_no-1, page_size);
//        Page<StaffinfoModel> list = staffinfoRepository.findAll(query, pageable);
        Page<StaffinfoModel> list = staffinfoRepository.findExistQywxStaffinfo(pageable);
        JSONObject result = new JSONObject();
        result.put("total_pages", list.getTotalPages());
        result.put("total_results", list.getTotalElements());
        result.put("list", list.getContent());
        return result;
    }

    private JSONObject queryStaffCustQywxRelation(JSONObject jsonparam) {
        int page_no = 1, page_size = 50;
        if (jsonparam.containsKey("page_no")) {
            page_no = jsonparam.getInteger("page");
        }
        if (jsonparam.containsKey("page_size")) {
            page_size = jsonparam.getInteger("page_size");
        }
        //查询有效员工数据
        StaffinfoModel staffinfoWhere = new StaffinfoModel();
        staffinfoWhere.setStatus("Y");
        Example<StaffinfoModel> query = Example.of(staffinfoWhere);
        int page = 1, size = 50;
        Pageable pageable = PageRequest.of(page-1,size);
        Page<StaffinfoModel> list = staffinfoRepository.findAll(query, pageable);
        JSONObject result = new JSONObject();
        result.put("total_pages", list.getTotalPages());
        result.put("total_results", list.getTotalElements());
        result.put("list", list.getContent());
        return result;
    }

    @Test
    void getCustment() {
        int page_no = 1, page_size = 2;
        JSONObject jsonparam = new JSONObject();
        jsonparam.put("page_no", page_no);
        jsonparam.put("page_size", page_size);
        JSONObject result = staffinfoService.queryStaffList(jsonparam);
        Integer total_pages = result.getInteger("total_pages");
        Integer total_results = result.getInteger("total_results");
        JSONArray list = result.getJSONArray("list");
        logger.info("查询到{}个员工信息", total_results);
        logger.info("总分页数：{}", total_pages);
        for (int i = 0; i < list.size(); i++) {
            StaffinfoModel staffinfo = JSONObject.parseObject(list.getJSONObject(i).toJSONString(), StaffinfoModel.class);
            logger.info("同步员工:{}的顾客关系", staffinfo.getName());
            int cnt = staffCustQywxRelationService.syncStaffCustQywxRelation(staffinfo);
            logger.info("完成员工:{}的顾客关系同步数量:{}", staffinfo.getName(), cnt);
        }
        page_no++;
        for (;page_no < total_pages; page_no++) {
            logger.info("查询第{}页数据", page_no);
            jsonparam.put("page_no", page_no);
            result = staffinfoService.queryStaffList(jsonparam);
            list = result.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                StaffinfoModel staffinfo = JSONObject.parseObject(list.getJSONObject(i).toJSONString(), StaffinfoModel.class);
                logger.info("同步员工:{}的顾客关系", staffinfo.getName());
                int cnt = staffCustQywxRelationService.syncStaffCustQywxRelation(staffinfo);
                logger.info("完成员工:{}的顾客关系同步数量:{}", staffinfo.getName(), cnt);
            }
        }

//        StaffinfoModel staffinfoWhere = new StaffinfoModel();
//        staffinfoWhere.setEnt_id(0L);
//        staffinfoWhere.setStatus("Y");
//        Example<StaffinfoModel> query = Example.of(staffinfoWhere);
//        int page = 1, size = 10;
//        Pageable pageable = PageRequest.of(page-1,size);
//        Page<StaffinfoModel> list = staffinfoRepository.findAll(query, pageable);
//        long totalElements = list.getTotalElements(); //总数
//        int totalPages = list.getTotalPages(); //页数
//        int size1 = list.getSize(); //当前条数
//        System.out.println(String.format("totalElements: %d, totalPages: %d, size: %d", totalElements, totalPages, size1));
//        for (StaffinfoModel staffinfo : list.getContent()) {
//            System.out.println(JSONObject.toJSONString(staffinfo));
//        }
    }

    @Test
    void getEncodingAESKey() {
        String encodingAESKey = Base64.encodeBase64String(UUID.randomUUID().toString().replaceAll("-","").getBytes());
        System.out.println(encodingAESKey);
    }

    @Test
    void getMsgResult() {
        String msgid = "msgid-xxxxx";
        WxCpService wxCpService = WxCpConfiguration.getCpService(key2);
        WxCpMsgServiceImpl wxCpMsgService = new WxCpMsgServiceImpl(wxCpService);
        try {
            JSONObject result = wxCpMsgService.getMsgResult(msgid);
            System.out.println(result.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }
    @Test
    void sendMsg() {
        String text = "这是测试消息";
        List<String> externalUserid = new ArrayList<>();
        externalUserid.add("xxxxee");
        externalUserid.add("eeeexx");
        WxCpService wxCpService = WxCpConfiguration.getCpService(key1);
        WxCpMsgServiceImpl wxCpMsgService = new WxCpMsgServiceImpl(wxCpService);
        try {
            JSONObject result = wxCpMsgService.addMsg(externalUserid, userId, text, null, null, null);
            System.out.println(result.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUserInfo() {
        WxCpService wxCpService = WxCpConfiguration.getCpService(key1);
        WxCpUserServiceImpl wxCpUserService = new WxCpUserServiceImpl(wxCpService);
        try {
            WxCpUser user = wxCpUserService.getById(userId);
            System.out.println(JSONObject.toJSONString(user));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    //查询员工所有联系人
    @Test
    void getCpExternalContact() {
        String userId = "WangYe";
        WxCpService wxCpService = WxCpConfiguration.getCpService(key2);
        WxCpExternalContactServiceImpl wxCpExternalContactService = new WxCpExternalContactServiceImpl(wxCpService);
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<String> externalContacts = wxCpExternalContactService.listExternalContacts(userId);
            for (String code : externalContacts) {
                WxCpUserExternalContactInfo detail = wxCpExternalContactService.getContactDetail(code);
                Thread.sleep(1000);
                if (detail.getExternalContact().getType() == 1) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", detail.getExternalContact().getName());
                    map.put("unionid", detail.getExternalContact().getUnionId());
                    map.put("gender", detail.getExternalContact().getGender());
                    map.put("avatar", detail.getExternalContact().getAvatar());
                    list.add(map);
                }
//                System.out.println(JSONObject.toJSONString(detail));
            }
        } catch (WxErrorException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JSONObject.toJSONString(list));
        System.out.println("ok");
    }

    @Test
    void sendMessageTask() {
        ServiceSession session = new ServiceSession();
        session.setEnt_id(0L);
        List<String> list = qywxMessageService.getMsgIdsByStatus(session, "N");

        WxCpService wxCpService = WxCpConfiguration.getCpService(key2);
        WxCpMsgServiceImpl wxCpMsgService = new WxCpMsgServiceImpl(wxCpService);
        for (String msgid : list) {
            try {
                JSONObject result = wxCpMsgService.getMsgResult(msgid);
                System.out.println(result.toJSONString());
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        System.out.println(JSONObject.toJSONString(list));
    }

    @Test
    void CpChatTest() {
        WxCpService wxCpService = WxCpConfiguration.getCpService(key1);
        WxCpChatServiceImpl wxCpChatService = new WxCpChatServiceImpl(wxCpService);
        List<String> users = new ArrayList<>();
        users.add("uuuxxx");
        try {
            String ret = wxCpChatService.chatCreate("测试群聊", "xxx", users, "xxx");
            System.out.println(ret);
        }catch (WxErrorException e) {
            e.printStackTrace();
        }
        System.out.println("ok");
    }

    @Test
    void test() {
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId("xxx");      // 设置微信企业号的appid
        config.setCorpSecret("xxxx");  // 设置微信企业号的app corpSecret
        config.setAgentId(1000006);     // 设置微信企业号应用ID
        config.setToken("xxx");       // 设置微信企业号应用的token
        config.setAesKey("xxx");      // 设置微信企业号应用的EncodingAESKey


        WxCpMessage
                .TEXT()
                .agentId(1000006) // 企业号应用ID
                 .toUser("非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送")
                .toParty("非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .toTag("非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数")
                .content("sfsfdsdf")
                .build();
    }


    @Test
    public void testDays() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expireDate = sdf.parse("2020-07-01");
//        Date edate = sdf.parse("2020-01-01");
        long days = LocalDate.now().until(expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), ChronoUnit.DAYS);
        System.out.println("间隔时间:"+ days);

//        LocalDate startDate = sdate.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
//        LocalDate endDate = edate.toInstant().atZone(ZoneId.of("Asia/Shanghai")).toLocalDate();
//        long days = endDate.until(startDate, ChronoUnit.DAYS);
//        System.out.println("间隔时间:"+ days);
//        days = ChronoUnit.DAYS.between(startDate, endDate);
//        System.out.println("间隔时间:"+ days);

//        Calendar beginCalendar = Calendar.getInstance();
//		beginCalendar.setTime(sdate);
//        Calendar endCalendar = Calendar.getInstance();
//        endCalendar.setTime(edate);
//
//        int days = endCalendar.get(Calendar.DAY_OF_YEAR)-beginCalendar.get(Calendar.DAY_OF_YEAR);
//        System.out.println("间隔时间:"+ days);
//        if(endCalendar.get(Calendar.YEAR)!=beginCalendar.get(Calendar.YEAR)) {
//            do {
//                days+=endCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
//                endCalendar.add(Calendar.YEAR, 1);
//            }
//            while(endCalendar.get(Calendar.YEAR)!=beginCalendar.get(Calendar.YEAR));
//        }
//        System.out.println("间隔时间:"+ days);
    }

}
