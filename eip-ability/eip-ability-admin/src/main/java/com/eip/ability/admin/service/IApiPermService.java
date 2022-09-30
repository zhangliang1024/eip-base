package com.eip.ability.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.dto.ApiPermDTO;
import com.eip.ability.admin.domain.entity.baseinfo.ApiPerm;
import com.eip.ability.admin.domain.enums.DataScopeType;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * ClassName: IApiPermService
 * Function:
 * Date: 2022年09月30 13:23:07
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public interface IApiPermService extends SuperService<ApiPerm> {


    void addApi(ApiPermDTO data);

    void updateApi(Long id, ApiPermDTO data);

    void removeByApiId(Long roleId);

    IPage<ApiPerm> queryList(Integer current, Integer size, String name, String apiMethod, String apiPath, Boolean locked, String scopeType);
}
