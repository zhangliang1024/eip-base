package com.eip.ability.admin.controller.common;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eip.ability.admin.domain.PageRequest;
import com.eip.ability.admin.domain.dto.DictionaryDTO;
import com.eip.ability.admin.domain.entity.common.Dictionary;
import com.eip.ability.admin.domain.entity.common.DictionaryItem;
import com.eip.ability.admin.log.SysLog;
import com.eip.ability.admin.mybatis.wraps.Wraps;
import com.eip.ability.admin.service.DictionaryItemService;
import com.eip.ability.admin.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.eip.ability.admin.domain.converts.DictionaryConverts.DICTIONARY_DTO_2_PO_CONVERTS;


/**
 * 字典类型
 *
 * @author Levin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/dictionaries")
@Tag(name = "字典类型", description = "字典类型")
@RequiredArgsConstructor
public class DictionaryController {


    private final DictionaryService dictionaryService;
    private final DictionaryItemService dictionaryItemService;

    @GetMapping
    @SysLog(value = "字典查询")
    @Operation(description = "查询字典 - [DONE] - [Levin]")
    @Parameter(name = "name", description = "名称", in = ParameterIn.QUERY)
    public IPage<Dictionary> query(PageRequest pageRequest, String name, String code, Boolean status) {
        return this.dictionaryService.page(pageRequest.buildPage(),
                Wraps.<Dictionary>lbQ().eq(Dictionary::getStatus, status)
                        .like(Dictionary::getCode, code).like(Dictionary::getName, name));
    }

    @PostMapping
    @SysLog(value = "字典新增")
    @Operation(description = "新增字典 - [DONE] - [Levin]")
    public void save(@Validated @RequestBody DictionaryDTO dto) {
        this.dictionaryService.addDictionary(DICTIONARY_DTO_2_PO_CONVERTS.convert(dto));
    }

    @PutMapping("/{id}")
    @SysLog(value = "字典编辑")
    @Operation(description = "编辑字典 - [DONE] - [Levin]")
    public void edit(@PathVariable Long id, @Validated @RequestBody DictionaryDTO dto) {
        this.dictionaryService.editDictionary(DICTIONARY_DTO_2_PO_CONVERTS.convert(dto, id));
    }

    @DeleteMapping("/{id}")
    @SysLog(value = "删除指定字典项", request = true)
    @Operation(description = "删除字典 - [DONE] - [Levin]")
    public void del(@PathVariable Long id) {
        this.dictionaryService.deleteById(id);
    }


    @GetMapping("/{dictionary_code}/list")
    @Operation(description = "查询字典子项 - [DONE] - [Levin]")
    @Parameter(name = "dictionary_code", description = "编码", in = ParameterIn.PATH)
    public List<DictionaryItem> list(@PathVariable("dictionary_code") String dictionaryCode) {
        return this.dictionaryItemService.list(Wraps.<DictionaryItem>lbQ()
                .eq(DictionaryItem::getStatus, true).eq(DictionaryItem::getDictionaryCode, dictionaryCode));
    }
}
