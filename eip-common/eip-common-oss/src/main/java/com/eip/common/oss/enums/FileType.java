package com.eip.common.oss.enums;

/**
 * 支持七牛、阿里云、腾讯云、本地
 */
public enum  FileType implements IFileType {

    /**
     * 阿里云
     */
    ALIYUN("ALIYUN"),
    /**
     * 七牛云
     */
    QINIU("QINIU"),
    /**
     * 腾讯云
     */
    CLOUD("CLOUD"),
    /**
     * 本地
     */
    LOCAL("LOCAL")
    ;
    private String fileTyep;

    FileType(String fileTyep){
        this.fileTyep = fileTyep;
    }
    @Override
    public String getFileType() {
        return fileTyep;
    }
}
