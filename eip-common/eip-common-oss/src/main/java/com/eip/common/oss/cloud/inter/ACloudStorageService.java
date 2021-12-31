package com.eip.common.oss.cloud.inter;

import com.eip.common.oss.config.CloudStorageProperties;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.UUID;

public abstract class ACloudStorageService implements ICloudStorageService {

    /**
     * 云存储配置
     */
    protected CloudStorageProperties config;

    public static final String FILE_SEPARATOR = "/";

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateTime.now().toString("yyyyMMdd") + FILE_SEPARATOR + uuid;
        if(StringUtils.isNotBlank(prefix)){
            path = prefix + FILE_SEPARATOR + path;
        }
        return path + suffix;
    }



}
