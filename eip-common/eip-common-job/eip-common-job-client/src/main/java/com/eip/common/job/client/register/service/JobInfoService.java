package com.eip.common.job.client.register.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.eip.common.job.client.config.XxlJobAutoConfig;
import com.eip.common.job.client.register.model.XxlJobInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: JobInfoService
 * Function: 任务接口
 * Date: 2022年12月20 14:26:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class JobInfoService {

    @Autowired
    private JobLoginService jobLoginService;
    @Autowired
    private XxlJobAutoConfig properties;

    /**
     * 根据执行器id和jobHandler来查询任务列表
     */
    public List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler) {
        String url = properties.getAdminAddresses() + "/jobinfo/pageList";
        HttpResponse response = HttpRequest.post(url)
                .form("jobGroup", jobGroupId)
                .form("executorHandler", executorHandler)
                .form("triggerStatus", -1)
                .cookie(jobLoginService.getCookie())
                .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        List<XxlJobInfo> list = array.stream()
                .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
                .collect(Collectors.toList());

        return list;
    }

    /**
     * 注册一个新任务，返回任务id
     */
    public Integer addJobInfo(XxlJobInfo xxlJobInfo) {
        String url = properties.getAdminAddresses() + "/jobinfo/add";
        Map<String, Object> paramMap = BeanUtil.beanToMap(xxlJobInfo);
        HttpResponse response = HttpRequest.post(url)
                .form(paramMap)
                .cookie(jobLoginService.getCookie())
                .execute();

        JSON json = JSONUtil.parse(response.body());
        Object code = json.getByPath("code");
        if (code.equals(200)) {
            return Convert.toInt(json.getByPath("content"));
        }
        throw new RuntimeException("[AUTO-JOB] xxl add jobInfo error!");
    }

}
