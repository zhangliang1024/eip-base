package com.eip.ability.admin.service;


import com.eip.ability.admin.domain.entity.common.Dictionary;
import com.eip.ability.admin.mybatis.supers.SuperService;

/**
 * <p>
 * 业务接口
 * 字典类型
 * </p>
 *
 * @author Levin
 * @date 2019-07-02
 */
public interface DictionaryService extends SuperService<Dictionary> {

    /**
     * 添加字典
     *
     * @param dictionary 字典信息
     */
    void addDictionary(Dictionary dictionary);

    /**
     * 删除字典
     *
     * @param id id
     */
    void deleteById(Long id);

    /**
     * 编辑字典
     *
     * @param dictionary 字典信息
     */
    void editDictionary(Dictionary dictionary);
}
