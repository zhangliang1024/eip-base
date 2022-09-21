//package com.eip.ability.admin.controller.message;
//
//import cn.hutool.core.collection.CollectionUtil;
//import com.alibaba.fastjson.JSON;
//import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
//import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
//import com.eip.ability.admin.domain.entity.message.StationMessage;
//import com.eip.ability.admin.domain.entity.tenant.Tenant;
//import com.eip.ability.admin.mapper.TenantMapper;
//import com.eip.ability.admin.mybatis.wraps.Wraps;
////import com.eip.ability.admin.mybatis.conditions.event.TenantDynamicDataSourceProcess;
//import com.eip.ability.admin.mybatis.properties.DatabaseProperties;
//import com.eip.ability.admin.mybatis.properties.MultiTenantType;
//import com.eip.ability.admin.service.StationMessageService;
////import com.eip.ability.admin.websocket.BaseWebSocketEndpoint;
////import com.eip.ability.admin.websocket.utils.SpringContextHolder;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import javax.websocket.*;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.util.List;
//
//
///**
// * 聊天室
// *
// * @author Levin
// * @since 2020/11/11
// */
//@Slf4j
//@Component
//@ServerEndpoint(value = "/message/{tenantCode}/{identifier}")
//public class StationMessageEndpoint extends BaseWebSocketEndpoint {
//
//    @OnOpen
//    public void openSession(@PathParam("tenantCode") String tenantCode, @PathParam(IDENTIFIER) String userId, Session session) {
//        connect(userId, session);
//        List<StationMessage> messages = null;
//        final DatabaseProperties properties = SpringContextHolder.getBean(DatabaseProperties.class);
//        final StationMessageService stationMessageService = SpringContextHolder.getBean(StationMessageService.class);
//        if (properties.getMultiTenant().getType() == MultiTenantType.DATASOURCE) {
//            log.info("WebSocket 租户编码 - {}", tenantCode);
//            final DataSource dataSource = SpringContextHolder.getBean(DataSource.class);
//            DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
//            log.info("所有的数据源信息 - {}", ds.getDataSources());
//            DynamicDataSourceContextHolder.poll();
//            final TenantMapper tenantMapper = SpringContextHolder.getBean(TenantMapper.class);
//            final Long count = tenantMapper.selectCount(Wraps.<Tenant>lbQ().eq(Tenant::getCode, tenantCode));
//            if (count == null || count == 0) {
//                //final TenantDynamicDataSourceProcess dataSourceProcess = SpringContextHolder.getBean(TenantDynamicDataSourceProcess.class);
//                //final String dsKey = dataSourceProcess.buildDb(tenantCode);
//                //log.debug("设置当前线程数据源 - {}", dsKey);
//                //DynamicDataSourceContextHolder.push(dsKey);
//                messages = stationMessageService.list(Wraps.<StationMessage>lbQ().eq(StationMessage::getMark, false)
//                        .eq(StationMessage::getReceiveId, userId).orderByAsc(StationMessage::getId));
//                DynamicDataSourceContextHolder.poll();
//                log.debug("清空当前线程数据源...");
//            }
//        } else {
//            messages = stationMessageService.list(Wraps.<StationMessage>lbQ().eq(StationMessage::getMark, false)
//                    .eq(StationMessage::getReceiveId, userId).orderByAsc(StationMessage::getId));
//        }
//        if (CollectionUtil.isEmpty(messages)) {
//            return;
//        }
//        messages.forEach(message -> senderMessage(userId, JSON.toJSONString(message)));
//    }
//
//    @OnMessage
//    public void onMessage(@PathParam(IDENTIFIER) String userId, Session session, String message) {
//        log.info("接收到的消息" + message);
//    }
//
//    @OnClose
//    public void onClose(@PathParam(IDENTIFIER) String userId, Session session) {
//        disconnect(userId);
//    }
//
//    @OnError
//    public void onError(@PathParam(IDENTIFIER) String userId, Session session, Throwable throwable) {
//        log.info("发生异常：, identifier {} ", userId);
//        log.error(throwable.getMessage(), throwable);
//        disconnect(userId);
//    }
//
//}