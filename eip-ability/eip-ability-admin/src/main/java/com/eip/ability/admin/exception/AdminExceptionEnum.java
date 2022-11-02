package com.eip.ability.admin.exception;


import com.eip.common.core.core.BaseHttpCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 全局错误码 10000-15000
 * <p>
 * 预警异常编码    范围： 30000~34999
 * 标准服务异常编码 范围：35000~39999
 * 邮件服务异常编码 范围：40000~44999
 * 短信服务异常编码 范围：45000~49999
 * 权限服务异常编码 范围：50000-59999
 * 文件服务异常编码 范围：60000~64999
 * 日志服务异常编码 范围：65000~69999
 * 消息服务异常编码 范围：70000~74999
 * 开发者平台异常编码 范围：75000~79999
 * 搜索服务异常编码 范围：80000-84999
 * 共享交换异常编码 范围：85000-89999
 * 移动终端平台 异常码 范围：90000-94999
 * <p>
 * 安全保障平台    范围：        95000-99999
 * 软硬件平台 异常编码 范围：    100000-104999
 * 运维服务平台 异常编码 范围：  105000-109999
 * 统一监管平台异常 编码 范围：  110000-114999
 * 认证方面的异常编码  范围：115000-115999
 *
 * @author Levin
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AdminExceptionEnum implements AdminExceptionAssert {

    /**
     * 系统繁忙,请稍后再试
     */
    SYSTEM_BUSY("500", "系统繁忙,请稍后再试~"),
    SYSTEM_TIMEOUT("500", "系统维护中,请稍后再试~"),
    PARAM_EX("400", "参数类型解析异常"),
    SQL_EX("500", "运行SQL出现异常"),
    NULL_POINT_EX("500", "空指针异常"),
    INVALID_ARGUMENT_EX("400", "无效参数异常"),
    MEDIA_TYPE_EX("400", "请求类型异常"),
    LOAD_RESOURCES_ERROR("500", "加载资源出错"),
    BASE_VALID_PARAM("500", "统一验证参数异常"),
    OPERATION_EX("500", "操作异常"),
    SERVICE_MAPPER_ERROR("500", "Mapper类转换异常"),

    OK("200", "OK"),
    BAD_REQUEST("400", "错误的请求"),

    UNAUTHORIZED("401", "未经授权"),
    NOT_FOUND("404", "没有找到资源"),
    METHOD_NOT_ALLOWED("405", "不支持当前请求类型"),

    TOO_MANY_REQUESTS("429", "请求超过次数限制"),
    INTERNAL_SERVER_ERROR("500", "内部服务错误"),
    BAD_GATEWAY("502", "网关错误"),
    GATEWAY_TIMEOUT("504", "网关超时"),
    // 系统相关 end

    REQUIRED_FILE_PARAM_EX("1001", "请求中必须至少包含一个有效文件"),

    DATA_SAVE_ERROR("2000", "新增数据失败"),
    DATA_UPDATE_ERROR("2001", "修改数据失败"),
    TOO_MUCH_DATA_ERROR("2002", "批量新增数据过多"),
    // jwt token 相关 start

    JWT_TOKEN_EXPIRED("40001", "会话超时，请重新登录"),
    JWT_SIGNATURE("40002", "不合法的token，请认真比对 token 的签名"),
    JWT_ILLEGAL_ARGUMENT("40003", "缺少token参数"),
    JWT_GEN_TOKEN_FAIL("40004", "生成token失败"),
    JWT_PARSER_TOKEN_FAIL("40005", "解析token失败"),
    JWT_USER_INVALID("40006", "用户名或密码错误"),
    JWT_USER_ENABLED("40007", "用户已经被禁用！"),

    /*------------------------------业务异常--------------------------------*/
    USER_RECEIVER_TYPE_NOT_EMPTY(BaseHttpCode.BAD_REQUEST, "用户类型不能为空"),
    SESSION_MESSAGE_NOT_FOUND(BaseHttpCode.INTERNAL_SERVER_ERROR, "需要发布的消息不存在"),
    SESSION_MESSAGE_RECEIVE_NOT_EMPTY(BaseHttpCode.INTERNAL_SERVER_ERROR, "接受者不能为空"),
    VERFIY_CODE_NOT_EMPTY(BaseHttpCode.INTERNAL_SERVER_ERROR, "验证码key不能为空"),

    DICTIONARY_TYPE_REPEAT(BaseHttpCode.INTERNAL_SERVER_ERROR, "字典类型编码重复"),
    DICTIONARY_NOT_EMPTY(BaseHttpCode.INTERNAL_SERVER_ERROR, "字典内容不能为空"),
    DICTIONARY_CODE_HAVED_EXIST(BaseHttpCode.INTERNAL_SERVER_ERROR, "编码已存在"),
    DICTIONARY_NOT_FOUNT(BaseHttpCode.INTERNAL_SERVER_ERROR, "码表不存在"),
    TENANT_NOT_FOUNT(BaseHttpCode.INTERNAL_SERVER_ERROR, "租户不存在"),
    SYSTEM_TENANT_ROLE_NOT_FOUNT(BaseHttpCode.INTERNAL_SERVER_ERROR, "内置租户管理员角色不存在"),
    USER_NOT_FOUNT(BaseHttpCode.INTERNAL_SERVER_ERROR, "用户不存在"),
    DATASOURCE_DATA_NOT_FOUNT(BaseHttpCode.INTERNAL_SERVER_ERROR, "数据连接信息不存在"),


    DICTIONARY_NOT_FOUND(BaseHttpCode.INTERNAL_SERVER_ERROR, "字典不存在"),
    SYSTEM_ROLE_NOT_FOUND(BaseHttpCode.INTERNAL_SERVER_ERROR, "角色不存在"),
    SYSTEM_USER_NOT_FOUND(BaseHttpCode.INTERNAL_SERVER_ERROR, "账户不存在"),
    SYSTEM_DATA_DONOT_DELETE(BaseHttpCode.INTERNAL_SERVER_ERROR, "内置数据无法删除"),
    SYSTEM_ROLE_DONOT_DELETE(BaseHttpCode.INTERNAL_SERVER_ERROR, "内置角色无法删除"),
    SUPER_ROLE_DONOT_DELETE(BaseHttpCode.INTERNAL_SERVER_ERROR, "超级角色无法删除"),
    SYSTEM_USER_DONOT_DELETE(BaseHttpCode.INTERNAL_SERVER_ERROR, "内置用户不允许删除"),
    SYSTEM_ROLE_DONOT_EDIT(BaseHttpCode.INTERNAL_SERVER_ERROR, "内置角色无法编辑"),
    SUPER_ROLE_DONOT_EDIT(BaseHttpCode.INTERNAL_SERVER_ERROR, "超级角色无法编辑"),
    TENANT_HAS_BEEN_DISABLED(BaseHttpCode.INTERNAL_SERVER_ERROR, "租户已被禁用,请联系管理员"),
    SUPER_TENANT_DONOT_EDIT(BaseHttpCode.INTERNAL_SERVER_ERROR, "超级租户,禁止操作"),

    USER_NAME_NOT_EMPTY(BaseHttpCode.BAD_REQUEST, "账号名不能为空"),
    TENANT_CODE_NOT_EMPTY(BaseHttpCode.BAD_REQUEST, "租户编码不能为空"),
    DATA_NOT_EMPTY(BaseHttpCode.BAD_REQUEST, "信息不能为空"),

    DATASOURCE_NAME_HAVED_EXIST(BaseHttpCode.INTERNAL_SERVER_ERROR, "连接池名称已存在"),
    USER_ID_HAVED_EXIST(BaseHttpCode.INTERNAL_SERVER_ERROR, "客户ID已存在"),
    USER_NAME_HAVED_EXIST(BaseHttpCode.INTERNAL_SERVER_ERROR, "账号已存在"),
    USER_ORIGIN_PASSWORD_ERROR(BaseHttpCode.INTERNAL_SERVER_ERROR, "原始密码错误"),
    OAUTH2_DATA_NOT_FOUND(BaseHttpCode.INTERNAL_SERVER_ERROR, "认证信息不存在"),
    ;

    private String code;
    private String message;

    public AdminExceptionEnum param(Object... param) {
        message = String.format(message, param);
        return this;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
