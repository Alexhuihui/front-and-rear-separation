package top.alexmmd.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author 汪永晖
 */
@Slf4j
@Component
public class MailTools {

    @Autowired
    private JavaMailSender mailSender;

    // 注入常量
    @Value("${mail.fromMail.addr}")
    private String from;

    @Value("${spring.mail.username}")
    private String username; //发送邮件者

    /**
     * 发送邮件
     * @param title 标题
     * @param titleWithName 是否在标题后添加名称
     * @param content 邮件内容
     * @param contentWithName 是否在内容后添加名称
     * @param email 接收者的邮箱
     */
    private void sendNormalEmail(String title,boolean titleWithName,String content,boolean contentWithName,String email){
        String dName="T-door官方";
        MimeMessage mimeMessage=null;
        try{
            mimeMessage=mailSender.createMimeMessage(); //创建要发送的消息
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(new InternetAddress(username,dName,"UTF-8")); //设置发送者是谁
            helper.setTo(email); //接收者邮箱
            title=titleWithName?title+"-"+dName:title; //标题内容
            helper.setSubject(title); //发送邮件的标题
            if(contentWithName){
                content +="<p style='text-align:right'>"+dName+"</p>";
                content +="<p style='text-align:right'>"+RandomTools.curDate("yyyy-MM-dd HH:mm:ss")+"</p>";
            }
            helper.setText(content,true); //发送的内容 是否为HTML
        }catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }

    /**
     * 发送注册验证码
     * @param email 接收者的邮箱
     * @param code 验证码
     */
    public void sendRegisterCode(final String email,String code){
        final StringBuffer sb=new StringBuffer(); //实例化一个StringBuffer
        sb.append("<h2>"+"亲爱的"+email+"您好！</h2>").append("<p style='text-align: center; font-size: 24px; font-weight: bold'>您的注册验证码为:"+code+"</p>");
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendNormalEmail("验证码",true,sb.toString(),true,email);
            }
        }).start();
    }

    /**
     * 发送纯文本邮件
     *
     * @param toAddr  发送给谁
     * @param title   标题
     * @param content 内容
     */
    public void sendTextMail(String toAddr, String title, String content) {

        // 纯文本邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toAddr);
        message.setSubject(title);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("Text邮件已经发送。");
        } catch (Exception e) {
            log.error("发送Text邮件时发生异常！", e);
        }
    }

    /**
     * 发送 html 邮件
     *
     * @param toAddr
     * @param title
     * @param content 内容（HTML）
     */
    public void sendHtmlMail(String toAddr, String title, String content) {

        // html 邮件对象
        MimeMessage message = mailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddr);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("html邮件发送成功");
        } catch (Exception e) {
            log.error("发送html邮件时发生异常！", e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param toAddr
     * @param title
     * @param content
     * @param filePath 附件地址
     */
    public void sendAttachmentsMail(String toAddr, String title, String content, String filePath) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddr);
            helper.setSubject(title);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            //helper.addAttachment("test"+fileName, file);

            mailSender.send(message);
            log.info("带附件的邮件已经发送。");
        } catch (Exception e) {
            log.error("发送带附件的邮件时发生异常！", e);
        }
    }

    /**
     * 发送文本中有静态资源（图片）的邮件
     *
     * @param toAddr
     * @param title
     * @param content
     * @param rscPath 资源路径
     * @param rscId   资源id (可能有多个图片)
     */
    public void sendInlineResourceMail(String toAddr, String title, String content, String rscPath, String rscId) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toAddr);
            helper.setSubject(title);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            log.info("嵌入静态资源的邮件已经发送。");
        } catch (Exception e) {
            log.error("发送嵌入静态资源的邮件时发生异常！", e);
        }
    }
}
