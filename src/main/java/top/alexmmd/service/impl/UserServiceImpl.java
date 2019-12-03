package top.alexmmd.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.alexmmd.dao.CodeDao;
import top.alexmmd.dao.UserDao;
import top.alexmmd.domain.User;
import top.alexmmd.domain.VerificationCode;
import top.alexmmd.service.UserService;
import top.alexmmd.util.MailTools;
import top.alexmmd.util.RandomTools;
import top.alexmmd.util.RespCode;
import top.alexmmd.util.RespEntity;

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
}
