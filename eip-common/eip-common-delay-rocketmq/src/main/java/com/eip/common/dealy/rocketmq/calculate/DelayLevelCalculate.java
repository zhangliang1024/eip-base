package com.eip.common.dealy.rocketmq.calculate;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DelayLevelCalculate
 * Function: 延时等级计算
 * Date: 2022年03月04 13:12:24
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class DelayLevelCalculate {


    private static List<Integer> defaultLevel;

    /**
     * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h rocketMQ自动支持18个级别 全部转换为秒
     */
    static {
        defaultLevel = new ArrayList<>();
        defaultLevel.add(1);
        defaultLevel.add(5);
        defaultLevel.add(10);
        defaultLevel.add(30);
        defaultLevel.add(60);
        defaultLevel.add(120);
        defaultLevel.add(180);
        defaultLevel.add(240);
        defaultLevel.add(300);
        defaultLevel.add(360);
        defaultLevel.add(420);
        defaultLevel.add(480);
        defaultLevel.add(540);
        defaultLevel.add(600);
        defaultLevel.add(1200);
        defaultLevel.add(1800);
        defaultLevel.add(3600);
        defaultLevel.add(7200);
    }

    /**
     * 计算等级
     */
    public static Integer calculateDefault(long second) {
        Integer level = null;
        for (int i = defaultLevel.size() - 1; i >= 0; i--) {
            int l = (int) second / defaultLevel.get(i);
            if (l > 0 && level == null) {
                level = i;
            }
            if (level == null) {
                continue;
            }
            if (level < i) {
                break;
            }
        }
        return level + 1;
    }

    public static int calculateNum(long second) {
        for (int i = defaultLevel.size() - 1; i >= 0; i--) {
            int l = (int) second / defaultLevel.get(i);
            if (l != 0) {
                return l;
            }
        }
        return 0;
    }

    public static long get(Integer index) {
        return defaultLevel.get(index);
    }

    public static void main(String[] args) {
        System.out.println(calculateDefault(111700));
        System.out.println(calculateNum(1700));
    }

}
