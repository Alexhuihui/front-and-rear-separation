package top.alexmmd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alexmmd.service.UserService;
import top.alexmmd.util.RespEntity;

import java.util.Map;

/**
 * @author 汪永晖
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    private RespEntity respEntity = new RespEntity();

    @PostMapping("/register")
    public RespEntity register(@RequestBody Map<String, Object> map) {
        String e_mail = (String) map.get("email");
        String nickName = (String) map.get("nickName");
        String password = (String) map.get("password");
        String registerCode = (String) map.get("code");
        respEntity = userService.RegisterInfo(e_mail, nickName, password, registerCode);
        return respEntity;
    }

    @PostMapping("/sendCode")
    public RespEntity sendPollCode(@RequestBody Map<String, Object> map) {
        String email = (String) map.get("email");
        RespEntity respEntity = userService.sendCode(email);
        return respEntity;
    }

    @PostMapping("/login")
    public RespEntity login(@RequestBody Map<String, Object> map) {
        String email = (String) map.get("email");
        String password = (String) map.get("password");
        respEntity = userService.login(email, password);
        return respEntity;
    }
}
