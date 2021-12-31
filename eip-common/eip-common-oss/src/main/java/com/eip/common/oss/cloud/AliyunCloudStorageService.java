package com.eip.common.oss.cloud;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CreateBucketRequest;
import com.eip.common.oss.cloud.inter.ACloudStorageService;
import com.eip.common.oss.config.CloudStorageProperties;
import com.eip.common.oss.enums.FileType;
import com.eip.common.oss.exception.CloudBaseException;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 阿里云存储
 */
@Service
public class AliyunCloudStorageService extends ACloudStorageService {

    private OSSClient client;

    public AliyunCloudStorageService(CloudStorageProperties config){
        this.config = config;
        if(FileType.ALIYUN.getFileType().equals(config.getType())){
            init();
        }
    }

    //初始化
    private void init(){
        client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),config.getAliyunAccessKeySecret());
        //判断 bucketName 是否存在
        if (!client.doesBucketExist(config.getAliyunBucketName())) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(config.getAliyunBucketName());
            client.createBucket(createBucketRequest);
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject(config.getAliyunBucketName(), path, inputStream);
        } catch (Exception e){
            throw new CloudBaseException("File upload failed , Please check ali configuration properties");
        }
        return config.getAliyunDomain() + FILE_SEPARATOR + path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
    }

    @Override
    public FileType getFileType() {
        return FileType.ALIYUN;
    }
}
