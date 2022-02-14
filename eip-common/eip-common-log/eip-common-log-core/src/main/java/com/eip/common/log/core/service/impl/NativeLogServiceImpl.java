package com.eip.common.log.core.service.impl;

import cn.hutool.json.JSONObject;
import com.eip.common.core.utils.JacksonUtil;
import com.eip.common.log.core.config.LogProperties;
import com.eip.common.log.core.constant.LogConstans;
import com.eip.common.core.log.LogOperationDTO;
import com.eip.common.log.core.service.LogService;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * ClassName: NativeLogServiceImpl
 * Function:
 * Date: 2022年02月14 09:39:38
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(name = "eip.log.operation.pipeline", havingValue = LogConstans.NATIVE)
@ConditionalOnWebApplication
public class NativeLogServiceImpl implements LogService {

    @Autowired
    @Qualifier("logRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private ListeningExecutorService listeningExecutorService;

    @Override
    public boolean createLog(LogOperationDTO operationDTO) throws Exception {
        listeningExecutorService.submit(() -> {
            String body = JacksonUtil.objectToStr(operationDTO);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(body, requestHeaders);
            try {
                ResponseEntity response = restTemplate.postForEntity(LogConstans.SAVE_AUDIT_URL, request, Object.class);
                Object result = response.getBody();
                HttpStatus statusCode = response.getStatusCode();
                log.error("[Operation-Log] Send Native Service success : {}", result);
            } catch (Exception e) {
                log.error("[Operation-Log] Send Native Service Error : {}", e);
            }
        });
        return true;
    }
}
