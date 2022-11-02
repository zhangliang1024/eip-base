package com.eip.ability.admin.service.impl;


import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.eip.ability.admin.domain.entity.common.Dictionary;
import com.eip.ability.admin.domain.entity.common.DictionaryItem;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.DictionaryItemMapper;
import com.eip.ability.admin.mapper.DictionaryMapper;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 业务实现类
 * 字典类型
 * </p>
 *
 * @author zuihou
 * @date 2019-07-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl extends SuperServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    private final DictionaryItemMapper dictionaryItemMapper;

    @Override
    public void addDictionary(Dictionary dictionary) {
        AdminExceptionEnum.DICTIONARY_NOT_EMPTY.assertNotNull(dictionary);

        final Long count = this.baseMapper.selectCount(Wraps.<Dictionary>lbQ().eq(Dictionary::getCode, dictionary.getCode()));
        AdminExceptionEnum.DICTIONARY_TYPE_REPEAT.assertIsFalse(count != 0 && count > 0);

        this.baseMapper.insert(dictionary);
    }

    @DSTransactional
    @Override
    public void deleteById(Long id) {
        final Dictionary dictionary = Optional.ofNullable(this.baseMapper.selectById(id)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.DICTIONARY_NOT_FOUND.getMessage()));
        AdminExceptionEnum.SYSTEM_DATA_DONOT_DELETE.assertIsFalse(dictionary.getReadonly());

        this.baseMapper.deleteById(id);
        this.dictionaryItemMapper.delete(Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getDictionaryId, id));
    }

    @DSTransactional
    @Override
    public void editDictionary(Dictionary dictionary) {
        final Dictionary record =
                Optional.ofNullable(this.baseMapper.selectById(dictionary.getId())).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.DICTIONARY_NOT_FOUND.getMessage()));
        AdminExceptionEnum.SYSTEM_DATA_DONOT_DELETE.assertIsFalse(record.getReadonly());

        final Long count = this.baseMapper.selectCount(Wraps.<Dictionary>lbQ().ne(Dictionary::getId, dictionary.getId()).eq(Dictionary::getCode, dictionary.getCode()));
        AdminExceptionEnum.DICTIONARY_TYPE_REPEAT.assertIsFalse(count != 0 && count > 0);

        this.baseMapper.updateById(dictionary);
        this.dictionaryItemMapper.update(DictionaryItem.builder().status(dictionary.getStatus()).dictionaryCode(dictionary.getCode()).build(),
                Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getDictionaryId, dictionary.getId()));
    }


}
