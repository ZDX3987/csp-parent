package com.zdx.csp.message.adapter;

import com.alibaba.fastjson.JSONObject;

/**
 * 定义统一发送消息格式的接口
 * @author ZDX
 */
public interface MessageAdapter {
    public void sendMsg(JSONObject body);
}
