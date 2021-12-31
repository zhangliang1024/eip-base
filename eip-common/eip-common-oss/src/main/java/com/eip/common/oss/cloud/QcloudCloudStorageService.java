package com.eip.common.oss.cloud;

import com.eip.common.oss.cloud.inter.ACloudStorageService;
import com.eip.common.oss.config.CloudStorageProperties;
import com.eip.common.oss.enums.FileType;
import com.eip.common.oss.exception.CloudBaseException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云存储
 */
@Service
public class QcloudCloudStorageService extends ACloudStorageService {

    private COSClient client;

    public QcloudCloudStorageService(CloudStorageProperties config){
        this.config = config;
        if(FileType.CLOUD.getFileType().equals(config.getType())){
            init();
        }
    }

    //初始化
    private void init(){
    	Credentials credentials = new Credentials(config.getQcloudAppId(), config.getQcloudSecretId(),config.getQcloudSecretKey());
    	//初始化客户端配置
        ClientConfig qConfig = new ClientConfig();
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        qConfig.setRegion(config.getQcloudRegion());
    	client = new COSClient(qConfig, credentials);
    }

    @Override
    public String upload(byte[] data, String path) {
        //腾讯云必需要以"/"开头
        if(!path.startsWith(FILE_SEPARATOR)) {
            path = FILE_SEPARATOR + path;
        }
        //上传到腾讯云
        UploadFileRequest request = new UploadFileRequest(config.getQcloudBucketName(), path, data);
        String result = client.uploadFile(request);
        JSONObject object = JSONObject.fromObject(result);
        if(object.getInt("code") != 0) {
            throw new CloudBaseException("File upload failed : " + object.getString("message"));
        }
        return config.getQcloudDomain() + path;
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
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public FileType getFileType() {
        return FileType.CLOUD;
    }

}
