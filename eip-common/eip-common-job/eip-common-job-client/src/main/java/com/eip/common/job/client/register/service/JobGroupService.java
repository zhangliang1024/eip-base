package com.eip.common.job.client.register.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.eip.common.job.client.config.XxlJobAutoConfig;
import com.eip.common.job.client.register.model.XxlJobGroup;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ClassName: JobGroupService
 * Function: 任务执行器
 * Date: 2022年12月20 14:25:21
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class JobGroupService {

    @Autowired
    private JobLoginService jobLoginService;
    @Autowired
    private XxlJobAutoConfig properties;

    /**
     * 根据appName和执行器名称title查询
     */
    public List<XxlJobGroup> getJobGroup() {
        String url = properties.getAdminAddresses() + "/jobgroup/pageList";
        HttpResponse response = HttpRequest.post(url)
                .form("appname", properties.getAppName())
                .form("title", properties.getTitle())
                .cookie(jobLoginService.getCookie())
                .execute();
        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        List<XxlJobGroup> list = array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobGroup.class))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 判断当前执行器是否已经注册到调度中心
     */
    public boolean preciselyCheck() {
        List<XxlJobGroup> jobGroup = getJobGroup();
        Optional<XxlJobGroup> has = jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(properties.getAppName())
                        && xxlJobGroup.getTitle().equals(properties.getTitle()))
                .findAny();
        return has.isPresent();
    }

    /**
     * 注册新的executor到调度中心
     */
    public boolean autoRegisterGroup() {
        String url = properties.getAdminAddresses() + "/jobgroup/save";
        HttpResponse response = HttpRequest.post(url)
                .form("appname", properties.getAppName())
                .form("title", properties.getTitle())
                .cookie(jobLoginService.getCookie())
                .execute();
        Object code = JSONUtil.parse(response.body()).getByPath("code");
        return code.equals(200);
    }
}
