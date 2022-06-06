package com.eip.common.web.utils;

import cn.hutool.core.util.IdUtil;

/**
 * ClassName: FileNameUtil
 * Function:
 * Date: 2022年04月06 15:18:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class FileNameUtil {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return IdUtil.simpleUUID() + FileNameUtil.getSuffix(fileOriginName);
    }

}
