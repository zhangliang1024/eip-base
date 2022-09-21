package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.log.LoginLog;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 系统日志
 * </p>
 *
 * @author Levin
 * @since 2019-10-20
 */
@TenantDS
@Repository
public interface LoginLogMapper extends SuperMapper<LoginLog> {


    /**
     * 统计 IP 数据
     *
     * @return 统计结果
     */
    @Select("SELECT count(DISTINCT ( ip )) FROM common_login_log")
    long countDistinctLoginIp();

}
