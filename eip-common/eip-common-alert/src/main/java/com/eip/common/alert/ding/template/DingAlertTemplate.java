package com.eip.common.alert.ding.template;

/**
 * ClassName: DingAlertTemplate
 * Function: 告警模板
 * Date: 2021年12月16 15:04:56
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class DingAlertTemplate {

    /**
     * 服务上下线通知文本
     */
    public static final String DING_SERVER_ALARM_TXT =
            "<font color='#2a9d8f'>【**通知**】 </font>服务上下线变更动态通知 \n\n" +
                    " --- \n\n " +
                    "<font color='#708090' size=2>服务环境：【**%s**】</font> \n\n " +
                    "<font color='#708090' size=2>服务名称：%s</font> \n\n " +
                    "<font color='#708090' size=2>服务实例：%s</font> \n\n " +
                    "<font color='#778899' size=2>服务状态：%s</font> \n\n " +
                    "<font color='#778899' size=2>服务地址：%s</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>OWNER：@%s</font> \n\n" +
                    "<font color='#708090' size=2>提示：服务变更实时通知（无限制）</font> \n\n" +
                    " --- \n\n  " +
                    "**播报时间：%s**";

    /**
     * 线程池报警通知文本
     */
    public static final String DING_THREAD_ALARM_TXT =
            "<font color='#FF0000'>[警报] </font>%s - 动态线程池运行告警 \n\n" +
                    " --- \n\n " +
                    "<font color='#708090' size=2>线程池ID：%s</font> \n\n " +
                    "<font color='#708090' size=2>应用名称：%s</font> \n\n " +
                    "<font color='#778899' size=2>应用实例：%s</font> \n\n " +
                    "<font color='#778899' size=2>报警类型：%s</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>核心线程数：%d</font> \n\n " +
                    "<font color='#708090' size=2>最大线程数：%d</font> \n\n " +
                    "<font color='#708090' size=2>当前线程数：%d</font> \n\n " +
                    "<font color='#708090' size=2>活跃线程数：%d</font> \n\n " +
                    "<font color='#708090' size=2>最大任务数：%d</font> \n\n " +
                    "<font color='#708090' size=2>线程池任务总量：%d</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>队列类型：%s</font> \n\n " +
                    "<font color='#708090' size=2>队列容量：%d</font> \n\n " +
                    "<font color='#708090' size=2>队列元素个数：%d</font> \n\n " +
                    "<font color='#708090' size=2>队列剩余个数：%d</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>拒绝策略：%s</font> \n\n" +
                    "<font color='#708090' size=2>拒绝策略执行次数：</font><font color='#FF0000' size=2>%d</font> \n\n " +
                    "<font color='#708090' size=2>OWNER：@%s</font> \n\n" +
                    "<font color='#708090' size=2>提示：%d 分钟内此线程池不会重复告警（可配置）</font> \n\n" +
                    " --- \n\n  " +
                    "**播报时间：%s**";

    /**
     * 线程池参数变更通知文本
     */
    public static final String DING_THREAD_NOTICE_TXT =
            "<font color='#2a9d8f'>[通知] </font>%s - 动态线程池参数变更 \n\n" +
                    " --- \n\n " +
                    "<font color='#708090' size=2>线程池ID：%s</font> \n\n " +
                    "<font color='#708090' size=2>应用名称：%s</font> \n\n " +
                    "<font color='#778899' size=2>应用实例：%s</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>核心线程数：%s</font> \n\n " +
                    "<font color='#708090' size=2>最大线程数：%s</font> \n\n " +
                    "<font color='#708090' size=2>线程存活时间：%s / SECONDS</font> \n\n" +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>队列类型：%s</font> \n\n " +
                    "<font color='#708090' size=2>队列容量：%s</font> \n\n " +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>AGO 拒绝策略：%s</font> \n\n" +
                    "<font color='#708090' size=2>NOW 拒绝策略：%s</font> \n\n" +
                    " --- \n\n  " +
                    "<font color='#708090' size=2>提示：动态线程池配置变更实时通知（无限制）</font> \n\n" +
                    "<font color='#708090' size=2>OWNER：@%s</font> \n\n" +
                    " --- \n\n  " +
                    "**播报时间：%s**";
}
