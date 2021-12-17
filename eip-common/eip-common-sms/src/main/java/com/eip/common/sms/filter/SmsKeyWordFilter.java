package com.eip.common.sms.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * ClassName: SmsKeyWordFilter
 * Function: 敏感词过滤
 * Date: 2021年12月08 09:47:28
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class SmsKeyWordFilter {


    public static void main(String[] args) {
        String str = "我喜欢仓井空";
        System.out.println(isContains(str));
        System.out.println(getKeywordOfContains(str));
        System.out.println(getKeywordReplace(str));
    }

    /**
     * 大部分敏感词汇在10个以内，直接返回缓存的字符串
     */
    public static String[] starArr = {"*", "**", "***", "****", "*****", "******", "*******", "********", "*********", "**********"};

    /**
     * 敏感词汇列表
     */
    private static List<String> keywordList;

    static {
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException("Load keyword properties failed.", e);
        }
    }

    private static void init() throws Exception {
        Properties properties = new Properties();
        InputStream in = SmsKeyWordFilter.class.getClassLoader().getResourceAsStream("keyword.properties");
        properties.load(in);
        String keywords = properties.getProperty("sms.filter.keyword");
        if (StringUtils.isBlank(keywords)) {
            throw new RuntimeException("The keywords is empty.");
        }
        String[] words = keywords.split(",");
        keywordList = Arrays.asList(words);
        log.info("Load black words from file:[{}], load size:[{}] complate ...", keywordList.size());
    }

    /**
     * 短信内容是否包含敏感词汇
     *
     * @param content
     * @return
     */
    public static boolean isContains(String content) {
        if (StringUtils.isBlank(content)) {
            return false;
        }
        for (String keyword : keywordList) {
            if (content.toLowerCase().contains(keyword.toLowerCase())) {
                log.error("文本内容【" + content + "】包含敏感词汇：【" + keyword + "】");
                return true;
            }
        }
        return false;
    }

    /**
     * 判断短信内容是否包含敏感词汇，返回包含的敏感词汇
     *
     * @param content
     * @return
     */
    public static String getKeywordOfContains(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        for (String keyword : keywordList) {
            if (content.toLowerCase().contains(keyword.toLowerCase())) {
                log.error("文本内容【" + content + "】包含敏感词汇：【" + keyword + "】");
                return keyword;
            }
        }
        return "";
    }


    /**
     * @param content
     * @return
     * @description 将敏感内容替换成*，比如我是敏感词，替换后为我是***
     * @author jiaxiaozhen
     * @date 2019/9/25 10:39
     */
    public static String getKeywordReplace(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        int totalLength = 0;
        for (String keyword : keywordList) {
            if (content.toLowerCase().contains(keyword.toLowerCase())) {
                if (StringUtils.isBlank(keyword)) {
                    continue;
                }
                int length = keyword.length();
                // 大部分敏感词汇在10个以内，直接返回缓存的字符串
                if (length <= starArr.length) {
                    String replace = starArr[length - 1];
                    content = content.replace(keyword, replace);
                } else {
                    // 不在10个以内的重新生成n个*号的字符串，然后替换
                    char[] arr = new char[length];
                    for (int i = 0; i < length; i++) {
                        arr[i] = '*';
                    }
                    content = content.replace(keyword, new String(arr));
                }
                totalLength += length;
            }
            if (totalLength > content.length()) {
                break;
            }
        }
        return content;
    }

}
