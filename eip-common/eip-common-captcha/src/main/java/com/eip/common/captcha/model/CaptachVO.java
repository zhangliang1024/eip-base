package com.eip.common.captcha.model;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * ClassName: CaptachVO
 * Function:
 * Date: 2022年01月26 14:30:13
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
@Builder
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CaptachVO {

    private String captach;

    private String uid;

}
