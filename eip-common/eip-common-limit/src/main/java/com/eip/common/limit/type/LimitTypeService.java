package com.eip.common.limit.type;


import org.springframework.stereotype.Service;

/**
 * 限流类型接口
 */
@Service
public interface LimitTypeService {

    String getType();
}
