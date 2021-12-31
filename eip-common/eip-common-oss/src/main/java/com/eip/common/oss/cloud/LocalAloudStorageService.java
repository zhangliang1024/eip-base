package com.eip.common.oss.cloud;

import com.eip.common.oss.cloud.inter.ACloudStorageService;
import com.eip.common.oss.config.CloudStorageProperties;
import com.eip.common.oss.enums.FileType;
import com.eip.common.oss.exception.CloudBaseException;
import org.springframework.stereotype.Service;
import java.io.*;

/**
 * 本地存储
 */
@Service
public class LocalAloudStorageService extends ACloudStorageService {

    public LocalAloudStorageService(CloudStorageProperties config){
        this.config = config;
    }

    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            //检查该路径对应的目录是否存在. 如果不存在则创建目录
            String newFile = path.substring(0, path.lastIndexOf(FILE_SEPARATOR));
            File file = new File(newFile);
            if (!file.exists()  && !file.isDirectory() ) {
                file.mkdirs();
            }
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path))) {
                // 1K的数据缓冲
                byte[] bytes = new byte[1024];
                // 读取到的数据长度
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new CloudBaseException("File upload failed");
        }
        return path;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getLocalPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getLocalPrefix(), suffix));
    }

    @Override
    public FileType getFileType() {
        return FileType.LOCAL;
    }
}
