package com.eip.ability.admin.domain.converts;

import com.eip.ability.admin.domain.dto.DictionaryDTO;
import com.eip.ability.admin.domain.dto.DictionaryItemDTO;
import com.eip.ability.admin.domain.entity.common.Dictionary;
import com.eip.ability.admin.domain.entity.common.DictionaryItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author Levin
 */
@Slf4j
public class DictionaryConverts {

    public static final DictionaryConverts.DictionaryDto2PoConverts DICTIONARY_DTO_2_PO_CONVERTS = new DictionaryConverts.DictionaryDto2PoConverts();

    public static class DictionaryDto2PoConverts implements BasePageConverts<DictionaryDTO, Dictionary> {

        @Override
        public Dictionary convert(DictionaryDTO source, Long id) {
            if (source == null) {
                return null;
            }
            Dictionary target = new Dictionary();
            BeanUtils.copyProperties(source, target);
            target.setId(id);
            return target;
        }
    }

    public static final DictionaryConverts.DictionaryItemDto2ItemPoConverts DICTIONARY_ITEM_DTO_2_ITEM_PO_CONVERTS = new DictionaryConverts.DictionaryItemDto2ItemPoConverts();

    public static class DictionaryItemDto2ItemPoConverts implements BasePageConverts<DictionaryItemDTO, DictionaryItem> {

        @Override
        public DictionaryItem convert(DictionaryItemDTO source) {
            if (source == null) {
                return null;
            }
            DictionaryItem target = new DictionaryItem();
            BeanUtils.copyProperties(source, target);
            return target;
        }
    }

}
