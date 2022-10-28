package com.eip.ability.admin.controller.common;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.dto.DictionaryItemDTO;
import com.eip.ability.admin.domain.entity.common.DictionaryItem;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DictionaryItemService;
import com.eip.common.core.core.protocol.response.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.eip.ability.admin.domain.converts.DictionaryConverts.DICTIONARY_ITEM_DTO_2_ITEM_PO_CONVERTS;


/**
 * 字典项
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "字典类型", description = "字典类型")
@RequestMapping("/dictionaries/{dictionary_id}/items")
public class DictionaryItemController {

    private final DictionaryItemService dictionaryItemService;

    @GetMapping
    @Operation(description = "查询字典子项 - [DONE] - [Levin]")
    @Parameters({
            @Parameter(name = "dictionary_id", description = "字典ID", in = ParameterIn.PATH),
            @Parameter(name = "label", description = "名称", in = ParameterIn.QUERY)
    })
    public ApiResult<Page<DictionaryItem>> query(@PathVariable("dictionary_id") Long dictionaryId, String label, Boolean status, PageRequest params) {
        final Page<DictionaryItem> itemPage = this.dictionaryItemService.page(params.buildPage(), Wraps.<DictionaryItem>lbQ()
                .like(DictionaryItem::getLabel, label).eq(DictionaryItem::getStatus, status)
                .eq(DictionaryItem::getDictionaryId, dictionaryId));
        return ApiResult.success(itemPage);
    }

    @PostMapping
    @Operation(description = "添加字典子项 - [DONE] - [Levin]")
    @Parameter(name = "dictionary_id", description = "字典ID", in = ParameterIn.PATH)
    public void save(@PathVariable("dictionary_id") Long dictionaryId, @Validated @RequestBody DictionaryItemDTO dto) {
        this.dictionaryItemService.addDictionaryItem(dictionaryId, DICTIONARY_ITEM_DTO_2_ITEM_PO_CONVERTS.convert(dto));
        
    }

    @PutMapping("/{id}")
    @Operation(description = "编辑字典子项 - [DONE] - [Levin]")
    @Parameter(name = "id", description = "子项ID", in = ParameterIn.PATH)
    public void edit(@PathVariable("dictionary_id") Long dictionaryId, @PathVariable Long id, @Validated @RequestBody DictionaryItemDTO dto) {
        final DictionaryItem dictionaryItem = DICTIONARY_ITEM_DTO_2_ITEM_PO_CONVERTS.convert(dto);
        dictionaryItem.setId(id);
        this.dictionaryItemService.editDictionaryItem(dictionaryId, dictionaryItem);
        
    }

    @DeleteMapping("/{id}")
    @Operation(description = "删除字典子项 - [DONE] - [Levin]")
    @Parameter(name = "id", description = "子项ID", in = ParameterIn.PATH)
    public void del(@PathVariable Long id) {
        this.dictionaryItemService.removeById(id);
        
    }

}
