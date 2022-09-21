package com.eip.ability.admin.mapper;

import com.eip.ability.admin.domain.entity.common.Dictionary;
//import TenantDS;
import com.eip.ability.admin.mybatis.supers.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 字典类型
 * </p>
 *
 * @author Levin
 * @date 2019-07-02
 */
//@TenantDS
@Repository
public interface DictionaryMapper extends SuperMapper<Dictionary> {

}
