package com.eip.common.web.valid.annotaion.constraint;

import com.eip.common.core.utils.ApplicationContextUtils;
import com.eip.common.web.valid.annotaion.CustomVaild;
import com.eip.common.web.valid.extend.ValidHandler;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ClassName: CustomValidConstraint
 * Function: 自定义JSR303 校验约束对象
 * Date: 2022年01月10 14:20:39
 *
 * @author 张良 E-mail:zhangliang01@jingyougroup.com
 * @version V1.0.0
 */
public class CustomValidConstraint implements ConstraintValidator<CustomVaild,Object> {

    private CustomVaild customVaild;

    @Override
    public void initialize(CustomVaild customVaild) {
        this.customVaild = customVaild;
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        //获取实际校验器
        ValidHandler handler = ApplicationContextUtils.getBean(customVaild.handler());
        //执行校验
        return handler.handler(customVaild,obj);
    }
}
