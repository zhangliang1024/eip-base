package com.eip.sample.common.rocketmq;

import com.eip.common.base.rocketmq.domain.BaseMessage;
import lombok.Data;

import java.time.LocalDate;

/**
 * ClassName: MemberMessage
 * Function:
 * Date: 2023年07月20 15:37:25
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Data
public class MemberMessage extends BaseMessage {


    private String userName;
    private LocalDate birthday;
}
