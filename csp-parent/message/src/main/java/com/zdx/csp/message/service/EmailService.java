package com.zdx.csp.message.service;

import com.alibaba.fastjson.JSONObject;
import com.zdx.csp.message.adapter.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.subject}")
    private String subject;
    @Value("${msg.text}")
    private String text;
    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMsg(JSONObject body) {
        // 处理发送邮件
        String email=body.getString("email");
        String code = body.getString("code");
        if(StringUtils.isEmpty(email)){
            return ;
        }
        log.info("消息服务平台发送邮件:{}",email);

        //发送邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text.replace("{}", email).replace("code", code));
        javaMailSender.send(simpleMailMessage);
        log.info("消息服务平台发送邮件:{}完成", email);
    }
}
