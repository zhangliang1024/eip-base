package com.eip.base.business.web.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @项目名称：springboot
 * @包名：com.zhliang.springbootapiui.controller
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2020/5/5 16:05
 * @version：V1.0
 */
@RestController
@Tag(name = "用户管理相关接口")
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    @Operation(summary = "添加用户的接口",
            parameters = {
                    @Parameter(name = "username", description = "用户名", example = "李四"),
                    @Parameter(name = "address", description = "用户地址", example = "深圳", required = true)
            }
    )
    public User addUser(String username, @RequestParam(required = true) String address) {
        return new User();
    }

    @GetMapping("/")
    @Operation(summary = "根据id查询用户的接口",
            parameters = {@Parameter(name = "id", description = "用户id", example = "99", required = true)}
    )
    public User getUserById(@PathVariable Integer id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @PutMapping("/{id}")
    @Operation(summary = "根据id更新用户的接口")
    public User updateUserById(@RequestBody User user) {
        return user;
    }
}
