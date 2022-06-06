package com.eip.common.web.utils;

import com.eip.common.core.core.assertion.enums.ArgumentResponseEnum;
import com.eip.common.core.core.assertion.enums.BaseResponseEnum;
import com.eip.common.core.core.exception.BaseRuntimeException;
import com.eip.common.core.utils.FlieNamefilter;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * ClassName: UploadFileUtils
 * Function:
 * Date: 2022年05月06 19:06:29
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
public class UploadFileUtils {


    /**
     * 支持文件扩展名校验
     */
    private static final String arrsuffix[] = {"PNG", "JPG", "JPEG", "BMP", "GIF", "SVG", "PDF"};


    public static void upload(String uploadPath, List<MultipartFile> files) {
        ArgumentResponseEnum.VALID_ERROR.assertIsFalse(files.isEmpty());
        try {
            List<File> list = new ArrayList<>();
            for (MultipartFile file : files) {
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (!Arrays.asList(arrsuffix).contains(suffix.toUpperCase())) {
                    throw new BaseRuntimeException(ArgumentResponseEnum.VALID_ERROR);
                }
                //获取文件名称
                String filenameup = file.getOriginalFilename();
                //定义要上传文件 的存放路径
                String localPath = uploadPath + "/";
                filenameup = FileNameUtil.getFileName(filenameup);
                File dest = new File(FlieNamefilter.fileNameValidate(localPath), FlieNamefilter.fileNameValidate(filenameup));
                Files.createParentDirs(dest);
                file.transferTo(dest);//拷贝文件到指定路径储存
                list.add(dest);
            }

            // 封装参数
            Map<String, String> params = new HashMap<>();
            // 上传文件
            uploadImage(list, params);
            // 上传完成后，删除文件
            for (File file : list) {
                file.delete();
            }

        } catch (Exception e) {
            log.error("[BANG-FEGIN] 邦邦异常反馈调用异常: [{}]", e.getMessage(), e);
            throw new BaseRuntimeException(BaseResponseEnum.SERVER_BUSY);
        }

    }

    public static void uploadImage(List<File> list, Map<String, String> params) throws Exception {
        //MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //for (File file : list) {
        //    // 把文件转换成流对象FileBody
        //    FileBody bin = new FileBody(file);
        //    builder.addPart("files", bin);
        //}
        //if (params != null) {
        //    for (String key : params.keySet()) {
        //        builder.addPart(key, new StringBody(params.get(key), ContentType.create("text/plain", Consts.UTF_8)));
        //    }
        //}
        //HttpEntity entity = builder.build();
    }
}
