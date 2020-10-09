package com.efuture.wechat.cp.config;

import java.util.List;

import com.efuture.wechat.utils.JsonUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@ConfigurationProperties(prefix = "wechat.cp")
public class WxCpProperties {

  private List<Ent> ents;

  public List<Ent> getEnts() {
    return ents;
  }

  public void setEnts(List<Ent> ents) {
    this.ents = ents;
  }

  public static class Ent {
    private long ent_id;
    /**
     * 设置微信企业号的corpId
     */
    private String corpId;

    private List<AppConfig> appConfigs;

    public long getEnt_id() {
      return ent_id;
    }

    public void setEnt_id(long ent_id) {
      this.ent_id = ent_id;
    }

    public String getCorpId() {
      return corpId;
    }

    public void setCorpId(String corpId) {
      this.corpId = corpId;
    }

    public List<AppConfig> getAppConfigs() {
      return appConfigs;
    }

    public void setAppConfigs(List<AppConfig> appConfigs) {
      this.appConfigs = appConfigs;
    }
  }

  public static class AppConfig {
    /**
     * 设置微信企业应用的AgentId
     */
    private Integer agentId;

    /**
     * 设置微信企业应用的Secret
     */
    private String secret;

    /**
     * 设置微信企业号的token
     */
    private String token;

    /**
     * 设置微信企业号的EncodingAESKey
     */
    private String aesKey;

    public Integer getAgentId() {
      return agentId;
    }

    public void setAgentId(Integer agentId) {
      this.agentId = agentId;
    }

    public String getSecret() {
      return secret;
    }

    public void setSecret(String secret) {
      this.secret = secret;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public String getAesKey() {
      return aesKey;
    }

    public void setAesKey(String aesKey) {
      this.aesKey = aesKey;
    }
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
