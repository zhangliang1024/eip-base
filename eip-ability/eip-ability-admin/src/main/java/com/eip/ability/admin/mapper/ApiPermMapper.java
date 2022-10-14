package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.mybatis.annotation.TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName: InterfacePermMapper
 * Function:
 * Date: 2022年09月30 13:21:51
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@TenantDS
@Repository
public interface ApiPermMapper extends SuperMapper<ApiPerm> {

    List<ApiPerm> selectApiPermByUserId(Long userId);

}
