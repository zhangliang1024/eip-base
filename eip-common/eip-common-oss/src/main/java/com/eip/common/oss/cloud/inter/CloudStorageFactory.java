package com.eip.common.oss.cloud.inter;

import com.eip.common.oss.config.CloudStorageProperties;
import com.eip.common.oss.enums.FileType;
import com.eip.common.oss.enums.IFileType;
import com.eip.common.oss.exception.CloudBaseException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 云存储工厂类
 */
@Service
public class CloudStorageFactory {

    private static final Logger logger = LoggerFactory.getLogger(CloudStorageFactory.class);

    private static Map<IFileType, ACloudStorageService> serviceMap;
    @Autowired
    private List<ACloudStorageService> serviceList;
    @Autowired
    private CloudStorageProperties config;

    @PostConstruct
    public void init(){
        logger.info("CloudStorage service init , type is : {}",config.getType());
        if(CollectionUtils.isEmpty(serviceList)){
            return;
        }
        serviceMap = new ConcurrentHashMap<>();
        serviceList.forEach(service -> {
            IFileType fileType = service.getFileType();
            if(serviceMap.get(fileType) != null){
                throw new CloudBaseException("The same service has been implemented");
            }
            if(fileType.getFileType().equals(config.getType())){
                serviceMap.put(FileType.valueOf(config.getType()),service);
            }
        });
        logger.info("CloudStorage service init done");
    }

    public ACloudStorageService buildService(){
        return serviceMap.get(FileType.valueOf(config.getType()));
    }


}
