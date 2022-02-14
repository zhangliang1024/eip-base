


### 日志注解使用示例

```java
@AuditLog(level = LogLevelEnum.LEVEL_LOW, eventType = LogEventTypeEnum.EVENT_BUSINESS, operateObject = "智能建造-问题告警-根据品类种类查询工序", operateType = LogOperateTypeEnum.OPERATE_QUERY)
public ApiResult<List<EipProcessDTO>> queryProcess(@RequestBody EipProcessQuery query) {
    logger.audit("根据品类种类查询工序");
    List<EipProcessDTO> list = buildService.queryProcess(query);
    return ApiResult.ok(list);
}
```