package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.domain.entity.common.Dictionary;
import com.eip.ability.admin.domain.entity.common.DictionaryItem;
import com.eip.ability.admin.exception.AdminExceptionEnum;
import com.eip.ability.admin.exception.AdminRuntimeException;
import com.eip.ability.admin.mapper.DictionaryItemMapper;
import com.eip.ability.admin.mapper.DictionaryMapper;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DictionaryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 业务实现类
 * 字典项
 * </p>
 *
 * @author zuihou
 * @date 2019-07-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictionaryItemServiceImpl extends SuperServiceImpl<DictionaryItemMapper, DictionaryItem> implements DictionaryItemService {

    private final DictionaryMapper dictionaryMapper;


    @Override
    public void addDictionaryItem(Long dictionaryId, DictionaryItem item) {
        final long count = this.baseMapper.selectCount(Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getValue, item.getValue()).eq(DictionaryItem::getDictionaryId, dictionaryId));

        AdminExceptionEnum.DICTIONARY_CODE_HAVED_EXIST.assertIsFalse(count > 0);
        final Dictionary dictionary =
                Optional.ofNullable(this.dictionaryMapper.selectById(dictionaryId)).orElseThrow(() -> new AdminRuntimeException(AdminExceptionEnum.DICTIONARY_NOT_FOUNT.getMessage()));
        item.setDictionaryId(dictionaryId);
        item.setDictionaryCode(dictionary.getCode());
        this.baseMapper.insert(item);
    }

    @Override
    public void editDictionaryItem(Long dictionaryId, DictionaryItem item) {
        final long count =
                this.baseMapper.selectCount(Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getValue, item.getValue()).ne(DictionaryItem::getId, item.getId()).eq(DictionaryItem::getDictionaryId,
                        dictionaryId));
        AdminExceptionEnum.DICTIONARY_CODE_HAVED_EXIST.assertIsFalse(count > 0);
        this.baseMapper.updateById(item);
    }
}
