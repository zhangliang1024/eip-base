package com.eip.common.alert.wxchat.domain;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.eip.common.alert.AlertBasetDTO;
import com.eip.common.alert.enums.MessageType;
import lombok.Data;

import java.io.*;

/**
 * @类描述：图片类型消息
 */
@Data
public class WxChatImage extends AlertBasetDTO {

    @Override
    public MessageType getWxChatType() {
        return MessageType.IMAGE;
    }

    private String base64;

    private String md5;

    /**
     * 设置图片文件，改方法会自动转换设置base64和md5
     */
    public void setFile(File file) {
        if (file != null && file.exists() && file.exists()) {
            //文件存在
            byte[] data = null;
            // 读取图片字节数组
            try {
                InputStream in = new FileInputStream(file);
                data = new byte[in.available()];
                in.read(data);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            base64 = Base64.encode(data);
            md5 = DigestUtil.md5Hex(data);
        }
    }

    /**
     * 1.通过网上url的形式设置图片文件,会先通过url下载图片文件之后进行文件发送
     * 2.url不支持301或者302这种跳转到其他图片地址的形式
     */
    public void setDiskFile(String url) {
        String suffix = url.substring(url.lastIndexOf('.'));
        File tempFile = FileUtil.createTempFile(System.currentTimeMillis()+"",suffix, FileUtil.getTmpDir(),true);
        HttpUtil.downloadFile(url,tempFile);
        setFile(tempFile);
    }

    public void setFile(String url) {
        ImageBase64Md5 image = Base64Utils.ImageToBase64ByOnline(url);
        base64 = image.getBase64();
        md5 = image.getMd5();
    }


}
