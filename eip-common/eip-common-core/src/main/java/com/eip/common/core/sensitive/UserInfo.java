package com.eip.common.core.sensitive;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

    @Sensitive(type = SensitiveTypeEnum.NAME)
    private String name;

    @Sensitive(type = SensitiveTypeEnum.ID_NUM)
    private String idNum;

    @Sensitive(type = SensitiveTypeEnum.PHONE_NUM)
    private String phone;

    @Sensitive(type = SensitiveTypeEnum.CUSTOMER, prefixNoMaskLen = 3, suffixNoMaskLen = 2, symbol = "#")
    private String address;

    @Sensitive(prefixNoMaskLen = 1, suffixNoMaskLen = 2, symbol = "*")
    private String password;


    public static void main(String[] args) throws Exception{
        ObjectMapper om = new ObjectMapper();

        UserInfo userInfo = new UserInfo();
        userInfo.setName("Lucas");
        userInfo.setIdNum("123456789123456789");
        userInfo.setPhone("13066666666");
        userInfo.setAddress("深圳市南山区深圳湾一号6601");
        userInfo.setPassword("abc123qwe");

        String msg = om.writeValueAsString(userInfo);
        System.out.println(msg);
    }
}