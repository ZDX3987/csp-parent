package com.zdx.csp.user.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zdx.csp.api.service.UserService;
import com.zdx.csp.common.constants.Constans;
import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.common.service.ResponseService;
import com.zdx.csp.common.utils.MD5Util;
import com.zdx.csp.common.utils.TokenUtils;
import com.zdx.csp.entity.User;
import com.zdx.csp.user.mapper.UserMapper;
import com.zdx.csp.user.mq.RegisterMailboxProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
public class UserServiceImpl extends ResponseService implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;
    @Autowired
    private FastFileStorageClient fileStorageClient;

    @Value("${fastdfs.host}")
    private String host;
    @Value("${message.queue}")
    private String MESSAGESQUEUE;


    @Override
    public ResponseResult sendVerificationCode(@RequestBody String email) {
        JSONObject jsonObject = JSONObject.parseObject(email);
        //生成随机验证码
        int code = (int) ((Math.random()*9+1)*100000);
        //发送邮件
        String json = emailJson(jsonObject.getString("email"), String.valueOf(code));
        log.info("####会员服务推送消息到消息服务平台####json:{}", json);
        sendMsg(json);
        //将验证码存入Redis
        redisService.setString(Constans.VERI_CODE +"-"+ jsonObject.getString("email"),
                String.valueOf(code), Constans.VERI_CODE_TIME);
        return setResultSuccess("发送成功");
    }

    @Override
    public ResponseResult regist(@RequestBody String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String code = jsonObject.getString("code");
        User user = JSONObject.parseObject(jsonObject.getString("user"), User.class);
        //校验注册验证码是否正确
        String key = Constans.VERI_CODE +"-"+ user.getEmail();
        String r_code = (String) redisService.getString(key);
        if (r_code == null) {
            return setResultError("验证码无效或者过期");
        }

        if (!code.equals(r_code)) {
            return setResultError("验证码输入错误，请再试一次");
        }
        if(StringUtils.isEmpty(user.getPassword())) {
            return setResultError("密码不能为空！");
        }

        String newPassword = MD5Util.MD5(user.getPassword());
        user.setPassword(newPassword);
        Integer result = userMapper.insert(user);
        if (result <= 0) {
            return setResultError("用户注册失败！");
        }

        return setResultSuccess("用户注册成功！");
    }

    private String emailJson(String email, String code) {
        JSONObject rootJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", "MSG_EMAIL");
        JSONObject content = new JSONObject();
        content.put("email", email);
        content.put("code", code);
        rootJson.put("header", header);
        rootJson.put("content", content);
        return rootJson.toJSONString();
    }

    private void sendMsg(String json) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue, json);
    }

    @Override
    public ResponseResult login(@RequestBody User user) {
        //校验参数
        if (StringUtils.isEmpty(user.getUsername())) {
            return setResultError("用户名不能为空！");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return setResultError("密码不能为空！");
        }

        //验证数据库

        String newPassword = MD5Util.MD5(user.getPassword());
        user.setPassword(newPassword);
        User userInfo = null;
        //验证用户名登录或邮箱登录
        if (user.getUsername().contains("@")) {
            user.setEmail(user.getUsername());
            user.setUsername(null);
            userInfo = userMapper.login(user);
        }
        else {
            userInfo = userMapper.login(user);
        }
        //登录失败
        if (userInfo == null) {
            return setResultError("用户名或密码错误！");
        }
        //登录成功，生成token
        String userToken = TokenUtils.getUserToken();
        log.info("####用户信息token存放在redis中... key为:{},value:{}", userToken, userInfo.getId());
        redisService.setString(userToken, userInfo.getId().toString(), Constans.TOKEN_USER_TIME);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userToken", userToken);
        return setResultSuccess(jsonObject);
    }

    @Override
    public ResponseResult findByToken(String token) {
        JSONObject jsonObject = JSONObject.parseObject(token);
        String code = jsonObject.getString("token");
        if (StringUtils.isEmpty(code)) {
            return setResultError("Token不能为空！");
        }
        //通过Token查找用户ID
        String userId = (String) redisService.getString(code);
        if (StringUtils.isEmpty(userId)) {
            return setResultError("Token无效或者已经过期！");
        }
        User user = userMapper.findById(Integer.parseInt(userId));

        if (user == null) {
           return setResultError("未查找到用户信息！");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseResult findById(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        if (id == null) {
            return setResultError("查询失败！");
        }
        User user = userMapper.findById(id);
        if (user == null) {
            return setResultError("未查找到用户信息！");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseResult modifyInfo(User user) {
        userMapper.update(user);
        return setResultSuccess("请求成功");
    }

    @Override
    public ResponseResult uploadImg(Integer id, MultipartFile file) {
        if (id == null || file == null) {
            return setResultError("头像上传失败！");
        }
        try {
            StorePath path = fileStorageClient.uploadFile(file.getInputStream(), file.getSize(),
                    FilenameUtils.getExtension(file.getOriginalFilename()), null);
            String imgUrl = "http://" + host +"/"+ path.getFullPath();
            log.info("imgUrl:{}", imgUrl);
            log.info("OriginalFilename:{}", file.getOriginalFilename());
            userMapper.updateUrl(imgUrl, id);
            return setResultSuccess("头像上传成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseResult changePassword(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String code = jsonObject.getString("code");
        if (email == null || password == null) {
            return setResultError("密码修改失败！");
        }
        User user = userMapper.findByEmail(email);
        if (user == null) {
            return setResultError("密码修改失败！");
        }
        String key = Constans.VERI_CODE +"-"+ user.getEmail();
        String r_code = (String) redisService.getString(key);
        if (!code.equals(r_code)) {
            return setResultError("验证码输入错误");
        }
        if (r_code == null) {
            return setResultError("验证码无效或者过期");
        }
        userMapper.changePassword(MD5Util.MD5(password), email);
        return setResultSuccess("密码修改成功！");
    }

    @Override
    public ResponseResult attentioned(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer from = jsonObject.getInteger("from");
        Integer to = jsonObject.getInteger("to");
        if (from == null || to == null) {
            return setResultError("查询失败！");
        }
        Integer attentioned = userMapper.attentioned(from, to);
        return setResultSuccess(attentioned);
    }

    @Override
    public ResponseResult attentionUser(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Boolean hasAttention = jsonObject.getBoolean("hasAttention");
        Integer from = jsonObject.getInteger("from");
        Integer to = jsonObject.getInteger("to");
        if (from == null || to == null) {
            return setResultError("关注失败！");
        }
        if (hasAttention) {
            userMapper.unAttention(from, to);
            return setResultSuccess("取消关注成功！");
        }
        else {
            Integer attention = userMapper.attention(from, to);
            if (attention == null) {
                return setResultError("关注失败！");
            }
            return setResultSuccess("关注成功！");
        }
    }

    @Override
    public ResponseResult getFriends(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer flag = jsonObject.getInteger("flag");
        Integer userId = jsonObject.getInteger("userId");
        if (flag == null || userId == null) {
            return setResultError("查找失败！");
        }
        List<Integer> friends = userMapper.getFriends(userId, flag);
        Iterator<Integer> iterator = friends.iterator();
        List<User> users = new ArrayList<>();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            User user = userMapper.findById(id);
            users.add(user);
        }
        return setResultSuccess(users);
    }

    @Override
    public ResponseResult logout(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String token = jsonObject.getString("token");
        if (StringUtils.isEmpty(token)) {
            return setResultError("退出失败！");
        }
        redisService.delKey(token);
        return setResultSuccess("退出成功！");
    }


}
