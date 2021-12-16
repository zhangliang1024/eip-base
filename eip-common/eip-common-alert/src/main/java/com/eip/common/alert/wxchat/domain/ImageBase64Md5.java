package com.eip.common.alert.wxchat.domain;

import lombok.Data;

/**
 * @类描述：图像加密
 */
@Data
public class ImageBase64Md5 {

	private String base64;
	private String md5;

	public ImageBase64Md5(String base64,String md5) {
        this.base64 = base64;
        this.md5 = md5;
    }

}
