package top.alexmmd.service;

import top.alexmmd.util.RespEntity;

/**
 * @author 汪永晖
 */
public interface UserService {

    /**
     * 发送验证码
     * @param email
     * @return
     */
    RespEntity sendCode(String email);

    /**
     * 注册信息提交
     * @param email
     * @param nickName
     * @param password
     * @param registerCode
     * @return
     */
    RespEntity RegisterInfo(String email,String nickName,String password,String registerCode);
}
