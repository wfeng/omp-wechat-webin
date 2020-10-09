package com.efuture.wechat.cp.config;

import java.util.Map;
import javax.annotation.PostConstruct;

import com.efuture.wechat.config.RedisConfig;
import me.chanjar.weixin.cp.config.impl.WxCpRedisConfigImpl;
import me.chanjar.weixin.cp.constant.WxCpConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.efuture.wechat.cp.handler.ContactChangeHandler;
import com.efuture.wechat.cp.handler.EnterAgentHandler;
import com.efuture.wechat.cp.handler.LocationHandler;
import com.efuture.wechat.cp.handler.LogHandler;
import com.efuture.wechat.cp.handler.MenuHandler;
import com.efuture.wechat.cp.handler.MsgHandler;
import com.efuture.wechat.cp.handler.NullHandler;
import com.efuture.wechat.cp.handler.SubscribeHandler;
import com.efuture.wechat.cp.handler.UnsubscribeHandler;
import com.google.common.collect.Maps;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.message.WxCpMessageRouter;
import redis.clients.jedis.JedisPool;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@EnableConfigurationProperties(WxCpProperties.class)
public class WxCpConfiguration {
    private LogHandler logHandler;
    private NullHandler nullHandler;
    private LocationHandler locationHandler;
    private MenuHandler menuHandler;
    private MsgHandler msgHandler;
    private UnsubscribeHandler unsubscribeHandler;
    private SubscribeHandler subscribeHandler;

    private WxCpProperties properties;

    @Autowired
    private RedisConfig redisConfig;

    private static JedisPool jedisPool;

    private static Map<String, WxCpMessageRouter> routers = Maps.newHashMap();
    private static Map<String, WxCpService> cpServices = Maps.newHashMap();

    private static final Logger logger = LoggerFactory.getLogger(WxCpConfiguration.class);

    @Autowired
    public WxCpConfiguration(LogHandler logHandler, NullHandler nullHandler, LocationHandler locationHandler,
                             MenuHandler menuHandler, MsgHandler msgHandler, UnsubscribeHandler unsubscribeHandler,
                             SubscribeHandler subscribeHandler, WxCpProperties properties) {
        this.logHandler = logHandler;
        this.nullHandler = nullHandler;
        this.locationHandler = locationHandler;
        this.menuHandler = menuHandler;
        this.msgHandler = msgHandler;
        this.unsubscribeHandler = unsubscribeHandler;
        this.subscribeHandler = subscribeHandler;
        this.properties = properties;
    }


    public static Map<String, WxCpMessageRouter> getRouters() {
        return routers;
    }

    public static WxCpService getCpService(String agentId) {
        return cpServices.get(agentId);
    }

    @PostConstruct
    public void initServices() {
        try {
            jedisPool = redisConfig.redisPoolFactory();
        } catch (Exception e) {
            logger.error("redis配置错误", e);
        }

        for (WxCpProperties.Ent ent : this.properties.getEnts()) {
            for (WxCpProperties.AppConfig appConfig : ent.getAppConfigs()) {
                WxCpRedisConfigImpl configStorage = new WxCpRedisConfigImpl(jedisPool);
                configStorage.setCorpId(ent.getCorpId());
                configStorage.setAgentId(appConfig.getAgentId());
                configStorage.setCorpSecret(appConfig.getSecret());
                configStorage.setToken(appConfig.getToken());
                configStorage.setAesKey(appConfig.getAesKey());
                WxCpService service = new WxCpServiceImpl();
                service.setWxCpConfigStorage(configStorage);
                String key = ent.getEnt_id() + "_" + appConfig.getAgentId();
                routers.put(key, this.newRouter(service));
                cpServices.put(key, service);
            }
        }
//
//        cpServices = this.properties.getAppConfigs().stream().map(a -> {
//            WxCpDefaultConfigImpl configStorage = new WxCpDefaultConfigImpl();
//            configStorage.setCorpId(this.properties.getCorpId());
//            configStorage.setAgentId(a.getAgentId());
//            configStorage.setCorpSecret(a.getSecret());
//            configStorage.setToken(a.getToken());
//            configStorage.setAesKey(a.getAesKey());
//            WxCpService service = new WxCpServiceImpl();
//            service.setWxCpConfigStorage(configStorage);
//            routers.put(a.getAgentId(), this.newRouter(service));
//            return service;
//        }).collect(Collectors.toMap(service -> service.getWxCpConfigStorage().getAgentId(), a -> a));
    }

    private WxCpMessageRouter newRouter(WxCpService wxCpService) {
        final WxCpMessageRouter newRouter = new WxCpMessageRouter(wxCpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.MenuButtonType.CLICK).handler(this.menuHandler).end();

        // 点击菜单链接事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.MenuButtonType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler)
            .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.UNSUBSCRIBE)
            .handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.LOCATION).handler(this.locationHandler)
            .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION)
            .handler(this.locationHandler).end();

        // 扫码事件（这里使用了一个空的处理器，可以根据自己需要进行扩展）
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxConsts.EventType.SCAN).handler(this.nullHandler).end();

        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxCpConsts.EventType.CHANGE_CONTACT).handler(new ContactChangeHandler()).end();

        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT)
            .event(WxCpConsts.EventType.ENTER_AGENT).handler(new EnterAgentHandler()).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
}
