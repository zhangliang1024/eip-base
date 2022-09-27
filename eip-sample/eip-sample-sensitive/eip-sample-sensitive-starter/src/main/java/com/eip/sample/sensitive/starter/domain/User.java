package com.eip.sample.sensitive.starter.domain;

import com.eip.cloud.eip.common.sensitive.annotation.Sensitive;
import com.eip.cloud.eip.common.sensitive.annotation.SensitiveFilter;
import com.eip.cloud.eip.common.sensitive.enums.SensitiveStrategy;
import com.eip.sample.sensitive.starter.handler.CustomSensitiveHandler;
import lombok.Builder;
import lombok.Data;

/**
 * ClassName: User
 * Function:
 * Date: 2022年09月27 13:48:42
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
public class User {

    /**
     * 姓名
     */
    @Sensitive(type = SensitiveStrategy.CHINESE_NAME)
    private String username;
    /**
     * 密码
     */
    @Sensitive(type = SensitiveStrategy.PASSWORD)
    private String password;
    /**
     * 邮箱
     */
    @Sensitive(type = SensitiveStrategy.EMAIL)
    private String email;
    /**
     * 固定电话
     */
    @Sensitive(type = SensitiveStrategy.FIXED_PHONE)
    private String fixedPhone;
    /**
     * 手机号
     */
    @Sensitive(type = SensitiveStrategy.MOBILE_PHONE)
    private String phone;
    /**
     * 身份证
     */
    @Sensitive(type = SensitiveStrategy.ID_CARD)
    private String idcard;
    /**
     * 车牌
     */
    @Sensitive(type = SensitiveStrategy.CAR_LICENSE)
    private String carLicense;
    /**
     * 银行卡
     */
    @Sensitive(type = SensitiveStrategy.BANK_CARD)
    private String bankCard;
    /**
     * 地址
     */
    @Sensitive(type = SensitiveStrategy.ADDRESS)
    private String address;
    /**
     * 备注
     */
    @Sensitive(type = SensitiveStrategy.CUSTOMIZE_KEEP_LENGTH, prefix = 2, suffix = 3)
    private String remark;
    /**
     * 信息
     */
    @Sensitive(type = SensitiveStrategy.CUSTOMIZE_WORDS_FILTER)
    @SensitiveFilter(value = {"操", "你大爷", "废物", "不要逼脸"})
    private String message;
    /**
     * 信息
     */
    @Sensitive(type = SensitiveStrategy.CUSTOMIZE_HANDLER, value = CustomSensitiveHandler.class)
    private String text;


    public static User mock(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .idcard("530321199204074611")
                .email("zhangsan@126.com")
                .fixedPhone("01010010010")
                .phone("18888888888")
                .bankCard("9988002866797031")
                .carLicense("京A66666")
                .address("北京市东城区金融街一号")
                .remark("技术的价值在哪里，降本提效。体现在规范性、基础架构上面。让程序井井有条，即时随着业务的发展，不会加大研发的重复开发成本。")
                .message("操你大爷，你说你是不是个废物，真是够不要逼脸的")
                .text("来来来，一起跳舞吧")
                .build();
    }

}
