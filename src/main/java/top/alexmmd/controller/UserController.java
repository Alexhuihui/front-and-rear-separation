package top.alexmmd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alexmmd.annotation.CurrentUser;
import top.alexmmd.annotation.PassToken;
import top.alexmmd.annotation.UserLoginToken;
import top.alexmmd.domain.User;
import top.alexmmd.service.UserService;
import top.alexmmd.util.RespCode;
import top.alexmmd.util.RespEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 汪永晖
 */
@RestController
@Api(value = "/user", description = "用户注册登录等操作接口")
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    private RespEntity respEntity = new RespEntity();

    @PassToken
    @PostMapping("/register")
    @ApiOperation(value="注册", notes="注册")
    public RespEntity register(@RequestBody Map<String, Object> map) {
        String e_mail = (String) map.get("email");
        String nickName = (String) map.get("nickName");
        String password = (String) map.get("password");
        String registerCode = (String) map.get("code");
        respEntity = userService.RegisterInfo(e_mail, nickName, password, registerCode);
        return respEntity;
    }

    @PassToken
    @PostMapping("/sendCode")
    @ApiOperation(value="请求注册码", notes="注册时，填写邮箱后获取注册码")
    public RespEntity sendPollCode(@RequestBody Map<String, Object> map) {
        String email = (String) map.get("email");
        RespEntity respEntity = userService.sendCode(email);
        return respEntity;
    }

    @PassToken
    @PostMapping("/login")
    @ApiOperation(value="登录", notes="登录校验，成功返回 token")
    public RespEntity login(@RequestBody Map<String, Object> map) {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        respEntity = userService.login(email, password);
        return respEntity;
    }

    /**
     * 更改密码
     *
     * @param user
     * @param map
     * @return
     */
    @UserLoginToken
    @PostMapping("/changePassword")
    @ApiOperation(value="修改密码", notes="请求时必须在 header 的 Authorization 中添加 token 值")
    public RespEntity OperationPassword(@CurrentUser User user, @RequestBody Map<String, Object> map) {
        String email = user.getEmail();
        String newPassword = (String) map.get("newPassword");
        String usedPassword = (String) map.get("usedPassword");
        respEntity = userService.changePassword(email, usedPassword, newPassword);
        return respEntity;
    }

    /**
     * 测试拦截器是否设置值到 httpServletRequest 中
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/test")
    @UserLoginToken
    public RespEntity test(HttpServletRequest httpServletRequest) {
        Object object = httpServletRequest.getAttribute("current_user");
        User user = null;
        if (object instanceof User) {
            user = (User) object;
        }

        log.info(user.toString());
        return new RespEntity(RespCode.SUCCESS.getCode(), user);
    }
}
