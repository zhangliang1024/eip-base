package com.eip.common.oss.cloud;

import com.eip.common.oss.cloud.inter.ACloudStorageService;
import com.eip.common.oss.config.CloudStorageProperties;
import com.eip.common.oss.enums.FileType;
import com.eip.common.oss.exception.CloudBaseException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 */
@Service
public class QiniuCloudStorageService extends ACloudStorageService {

    private UploadManager uploadManager;

    private String token;

    public QiniuCloudStorageService(CloudStorageProperties config){
        this.config = config;
        if(FileType.QINIU.getFileType().equals(config.getType())){
            init();
        }
    }

    //初始化
    private void init(){
        Auth auth;
        Configuration cfg;
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey()).uploadToken(config.getQiniuBucketName());
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("File upload error：" + res.toString());
            }
        } catch (Exception e) {
            throw new CloudBaseException("File upload failed , Please check qiniu configuration properties");
        }
        return config.getQiniuDomain() + FILE_SEPARATOR + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new CloudBaseException("File upload failed");
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }

    @Override
    public FileType getFileType() {
        return FileType.QINIU;
    }
}
