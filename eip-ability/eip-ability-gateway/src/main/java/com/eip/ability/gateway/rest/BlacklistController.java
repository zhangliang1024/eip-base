package com.eip.ability.gateway.rest;

import com.eip.ability.gateway.config.rule.BlacklistHelper;
import com.eip.ability.gateway.domain.Result;
import com.eip.ability.gateway.rest.domain.BlacklistRule;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Levin
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway/rules/blacklist")
public class BlacklistController {

    private final BlacklistHelper blacklistHelper;

    @GetMapping
    public Result<List<BlacklistRule>> query() {
        return Result.success(blacklistHelper.query());
    }

    @PostMapping
    public Result<Void> saveOrUpdate(@RequestBody BlacklistRule rule) {
        blacklistHelper.saveOrUpdate(rule);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        blacklistHelper.delete(id);
        return Result.success();
    }

}
