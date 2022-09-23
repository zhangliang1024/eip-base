package com.eip.common.apidoc.prop;

import lombok.Data;

import java.util.Map;

/**
 * ClassName: ExternalDocs
 * Function:
 * Date: 2022年09月23 16:21:58
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class ExternalDocs {

    private String description;

    private String url;

    private Map<String, Object> extensions;

}
