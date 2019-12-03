package top.alexmmd.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.alexmmd.dao.CodeDao;
import top.alexmmd.dao.UserDao;
import top.alexmmd.domain.User;
import top.alexmmd.domain.VerificationCode;
import top.alexmmd.service.UserService;
import top.alexmmd.util.*;

import java.util.Date;

/**
 * @author 汪永晖
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CodeDao codeDao;

    @Autowired
    private MailTools sendEmailUtils;

    /**
     * 发送验证码
     *
     * @param email
     * @return
     */
    @Override
    public RespEntity sendCode(String email) {
        RespEntity respEntity = new RespEntity();
        VerificationCode verificationCode = new VerificationCode();
        try {
            String code = RandomTools.randomCode();//产生随机的验证码
            User user = new User();

            if (!userDao.findByEmail(email).isPresent()) {
                log.info("邮箱：" + email + "--验证码为:" + code);
                //修改数据库中的验证码
                if (codeDao.findByEmail(email).isPresent()) {
                    verificationCode = codeDao.findByEmail(email).get();
                    verificationCode.setCode(code);
                    verificationCode.setEmail(email);
                    codeDao.save(verificationCode);
                }

                verificationCode.setEmail(email);
                verificationCode.setCode(code);
                verificationCode.setTime(new Date());
                //保存验证码信息到数据库
                codeDao.save(verificationCode);

                //发送邮件开始 发送验证码
                sendEmailUtils.sendRegisterCode(email, code);

                respEntity = new RespEntity(RespCode.REGISTER_SEND);
            } else {
                respEntity = new RespEntity(RespCode.REGISTER_NOTS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return respEntity;
    }

    /**
     * 注册信息提交
     *
     * @param email
     * @param nickName
     * @param password
     * @param registerCode
     * @return
     */
    @Override
    public RespEntity RegisterInfo(String email, String nickName, String password, String registerCode) {
        RespEntity respEntity = new RespEntity();
        VerificationCode verificationCode = new VerificationCode();
        if (codeDao.findByEmail(email).isPresent()) {
            verificationCode = codeDao.findByEmail(email).get();
            if (registerCode.equals(verificationCode.getCode())) {
                //时间校验--暂略
                User user = new User(email, password, nickName);
                userDao.save(user);
                //删除验证码信息
                codeDao.delete(verificationCode);
                respEntity = new RespEntity(RespCode.REGISTER_SUCCESS);
            } else {
                respEntity = new RespEntity(RespCode.CODE_EXPIRED);
            }
        } else {
            respEntity = new RespEntity(RespCode.REGISTER_FAILED);
        }
        return respEntity;
    }

    /**
     * 登录验证
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    public RespEntity login(String email, String password) {
        RespEntity respEntity = new RespEntity();
        String token = "";
        if (userDao.findByEmailAndPassword(email, password).isPresent()) {
            token = TokenUtil.createJwtToken(email);
            respEntity = new RespEntity(RespCode.LOGIN_SUCCESS, token);
        } else {
            respEntity = new RespEntity(RespCode.LOGIN_FAILED);
        }
        return respEntity;
    }

    /**
     * 根据旧密码更改密码
     *
     * @param usedPassword
     * @return
     */
    @Override
    public RespEntity changePassword(String email, String usedPassword, String newPassword) {
        RespEntity respEntity = new RespEntity();
        User user = userDao.findByEmailAndPassword(email, usedPassword).orElse(new User());

        if (user.getEmail() == null) {
            respEntity = new RespEntity(RespCode.PASSWORD_FAILED);
        } else {
            user.setPassword(newPassword);
            userDao.save(user);
            respEntity = new RespEntity(RespCode.SUCCESS);
        }
        return respEntity;
    }
}
