package com.zdx.csp.message.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdx.csp.message.adapter.MessageAdapter;
import com.zdx.csp.message.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerDistribute {

    @Autowired
    private EmailService emailService;

    private MessageAdapter messageAdapter;

    /**
     * 监听MQ
     * @param json 监听获取到的消息
     */
    @JmsListener(destination = "mq-csp")
    public void distribute(String json) {
        log.info("#######接收消息内容:{}####", json);
        if (StringUtils.isEmpty(json)) {
            return;
        }
        JSONObject rootJSON = JSON.parseObject(json);
        JSONObject header = rootJSON.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");
        if (StringUtils.isEmpty(interfaceType)) {
           return;
        }

        // 判断接口类型是否为发邮件
        if (interfaceType.equals("MSG_EMAIL")) {
            messageAdapter = emailService;
        }
        if (messageAdapter == null) {
            return;
        }
        JSONObject contentJson = rootJSON.getJSONObject("content");
        messageAdapter.sendMsg(contentJson);

    }
}
