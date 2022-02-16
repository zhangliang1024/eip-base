package com.eip.ability.log.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eip.ability.log.business.model.LogOperate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: LogBusinessMapper
 * Function:
 * Date: 2022年02月15 16:37:32
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Mapper
public interface LogBusinessMapper extends BaseMapper<LogOperate> {

    int insertLog(@Param("operate") LogOperate operate);

}
