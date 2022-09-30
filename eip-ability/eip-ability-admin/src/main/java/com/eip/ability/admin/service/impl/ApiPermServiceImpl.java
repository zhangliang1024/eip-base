package com.eip.ability.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.domain.dto.ApiPermDTO;
import com.eip.ability.admin.domain.entity.baseinfo.*;
import com.eip.ability.admin.domain.enums.DataScopeType;
import com.eip.ability.admin.exception.CheckedException;
import com.eip.ability.admin.mapper.ApiPermMapper;
import com.eip.ability.admin.mybatis.TenantEnvironment;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.IApiPermService;
import com.eip.ability.admin.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * ClassName: ApiPermServiceImpl
 * Function:
 * Date: 2022年09月30 13:24:59
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiPermServiceImpl extends SuperServiceImpl<ApiPermMapper, ApiPerm> implements IApiPermService {

    private final TenantEnvironment tenantEnvironment;

    @Override
    public IPage<ApiPerm> queryList(Integer current, Integer size, String apiName, String apiMethod, String apiPath, Boolean locked, String scopeType) {
        return this.page(new Page<>(current, size),
                Wraps.<ApiPerm>lbQ()
                        .likeRight(StringUtils.isNotBlank(apiName), ApiPerm::getApiName, apiName)
                        .likeRight(StringUtils.isNotBlank(apiPath), ApiPerm::getApiPath, apiPath)
                        .eq(StringUtils.isNotBlank(apiMethod), ApiPerm::getApiMethod, apiMethod)
                        .eq(Objects.nonNull(locked), ApiPerm::getLocked, locked)
                        .eq(StringUtils.isNotBlank(scopeType), ApiPerm::getScopeType, scopeType));
    }

    @Override
    public void addApi(ApiPermDTO data) {
        ApiPerm apiPerm = BeanUtil.toBean(data, ApiPerm.class);
        apiPerm.setReadonly(false);
        super.save(apiPerm);
    }

    @Override
    public void updateApi(Long id, ApiPermDTO data) {
        ApiPerm apiPerm = BeanUtil.toBean(data, ApiPerm.class);
        if (apiPerm.getReadonly() != null && apiPerm.getReadonly()) {
            throw CheckedException.badRequest("内置角色无法编辑");
        }
        apiPerm.setId(id);
        baseMapper.updateById(apiPerm);
    }

    @Override
    public void removeByApiId(Long apiId) {
        final ApiPerm role = Optional.ofNullable(baseMapper.selectById(apiId))
                .orElseThrow(() -> CheckedException.notFound("角色不存在"));
        if (role.getReadonly()) {
            throw CheckedException.badRequest("内置角色无法删除");
        }
        baseMapper.deleteById(apiId);
    }


}
