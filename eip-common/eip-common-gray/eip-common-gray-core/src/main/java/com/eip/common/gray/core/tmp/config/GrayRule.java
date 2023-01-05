// package com.eip.common.gray.core.tmp.config;
//
// import com.alibaba.cloud.nacos.ribbon.NacosServer;
// import com.eip.common.gray.core.support.RibbonFilterContextHolder;
// import com.google.common.base.Optional;
// import com.netflix.loadbalancer.Server;
// import com.netflix.loadbalancer.ZoneAvoidanceRule;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
//
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;
//
// /**
//  * ClassName: GrayRule
//  * Function:
//  * Date: 2023年01月03 16:15:06
//  *
//  * @author 张良 E-mail:zhangliang01@jingyougroup.com
//  * @version V1.0.0
//  */
// @Slf4j
// public class GrayRule extends ZoneAvoidanceRule {
//
//
//     public static final String KEY_VERSION = "version";
//
//     private static final String KEY_VERSION_PROD = "prod";
//
//     public static final String KEY_HTTP_HEADER_VERSION = "x-version";
//
//     @Override
//     public Server choose(Object key) {
//         try {
//             // 从ThreadLoca中获取灰度标记
//             String grayVersion = RibbonFilterContextHolder.getCurrentContext().get(KEY_VERSION);
//             if (StringUtils.isBlank(grayVersion)) {
//                 grayVersion = KEY_VERSION_PROD;
//             }
//             log.info("[eip-gray] gray version: {}", grayVersion);
//             // 获取所有可用服务
//             List<Server> serverList = this.getLoadBalancer().getReachableServers();
//
//             // 灰度服务和普通服务
//             Map<String, List<Server>> serverMaps = serverList.stream().collect(Collectors.groupingBy(server -> {
//                 NacosServer nacosServer = (NacosServer) server;
//                 if (nacosServer.getMetadata().containsKey(KEY_VERSION)) {
//                     return nacosServer.getMetadata().get(KEY_VERSION);
//                 }
//                 return KEY_VERSION_PROD;
//             }));
//
//
//             // 灰度发布的服务
//             List<Server> grayServerList = new ArrayList<>();
//             // 正常的服务
//             List<Server> normalServerList = new ArrayList<>();
//
//             for (Server server : serverList) {
//                 NacosServer nacosServer = (NacosServer) server;
//                 String versionValue = nacosServer.getMetadata().get(KEY_VERSION);
//                 log.info("[eip-gray] gray version: {} , server:{}", versionValue, nacosServer.getInstance());
//
//                 if (nacosServer.getMetadata().containsKey(KEY_VERSION)) {
//                     grayVersion = versionValue;
//                     grayServerList.add(server);
//                 } else {
//                     normalServerList.add(server);
//                 }
//             }
//
//             serverMaps.put(KEY_VERSION, grayServerList);
//             serverMaps.put(KEY_VERSION_PROD, normalServerList);
//             // 选取服务
//             List<Server> servers = serverMaps.get(grayVersion);
//             return originChoose(servers, key, grayVersion);
//         } finally {
//             // 清除灰度标记
//             RibbonFilterContextHolder.clearCurrentContext();
//         }
//     }
//
//
//     private Server originChoose(List<Server> servers, Object key, String grayVersion) {
//         Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(servers, key);
//         log.info("[eip-gray] gray version: {} , choose server:{}", grayVersion, server);
//         if (server.isPresent()) {
//             return server.get();
//         }
//         return null;
//
//     }
// }
