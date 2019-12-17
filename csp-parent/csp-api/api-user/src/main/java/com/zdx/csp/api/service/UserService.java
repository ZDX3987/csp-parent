package com.zdx.csp.api.service;

import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.entity.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;

@RequestMapping(produces = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
public interface UserService {
    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     */
    @RequestMapping(value = "/sendCode")
    ResponseResult sendVerificationCode(String email);

    @RequestMapping(value = "/register")
    ResponseResult regist(String data);

    @RequestMapping(value = "/login")
    ResponseResult login(@RequestBody User user);

    @RequestMapping(value = "/getUser")
    ResponseResult findByToken(@RequestBody String token);

    @RequestMapping(value = "/getUserInfo")
    ResponseResult findById(@RequestBody String json);

    @RequestMapping(value = "/modify")
    ResponseResult modifyInfo(@RequestBody User user);

    @RequestMapping(value = "/uploadImg")
    ResponseResult uploadImg(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile file);

    @RequestMapping(value = "/password")
    ResponseResult changePassword(@RequestBody String json);

    @RequestMapping("/attentioned")
    ResponseResult attentioned(@RequestBody String data);

    @RequestMapping("/attention")
    ResponseResult attentionUser(@RequestBody String data);

    @RequestMapping("/friends")
    ResponseResult getFriends(@RequestBody String data);

    @RequestMapping(value = "/logout")
    ResponseResult logout(@RequestBody String json);
}
