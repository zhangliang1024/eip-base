package com.eip.common.sms.phone.vo;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 短信VO
 *
 * @author yinzl
 */
public class SmsMsgVO implements Serializable {

    private static final long serialVersionUID = 8453991134106771009L;

    /**
     * 流水号
     */
    private String serialId;
    /**
     * 短信模板
     */
    private String templateCode;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 手机号
     */
    private Set<String> mobiles;
    /**
     * 变量
     */
    private Map<String, String> vars;
    /**
     * 添加手机号
     *
     * @param mobile 手机号
     */
    public void addMobile(String mobile) {
        if (this.mobiles == null) {
            this.mobiles = new HashSet<>();
        }
        this.mobiles.add(mobile);
    }

    /**
     * 获取 短信模板
     *
     * @return templateCode 短信模板
     */
    public String getTemplateCode() {
        return this.templateCode;
    }

    /**
     * 设置 短信模板
     *
     * @param templateCode 短信模板
     */
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    /**
     * 获取 短信签名
     *
     * @return signName 短信签名
     */
    public String getSignName() {
        return this.signName;
    }

    /**
     * 设置 短信签名
     *
     * @param signName 短信签名
     */
    public void setSignName(String signName) {
        this.signName = signName;
    }

    /**
     * 获取 变量
     *
     * @return vars 变量
     */
    public Map<String, String> getVars() {
        return this.vars;
    }

    /**
     * 设置 变量
     *
     * @param vars 变量
     */
    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    /**
     * 获取 手机号
     *
     * @return mobiles 手机号
     */
    public Set<String> getMobiles() {
        return this.mobiles;
    }

    /**
     * 设置 手机号
     *
     * @param mobiles 手机号
     */
    public void setMobiles(Set<String> mobiles) {
        this.mobiles = mobiles;
    }

    /**
     * 获取 流水号
     *
     * @return serialId 流水号
     */
    public String getSerialId() {
        return this.serialId;
    }

    /**
     * 设置 流水号
     *
     * @param serialId 流水号
     */
    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }
}
