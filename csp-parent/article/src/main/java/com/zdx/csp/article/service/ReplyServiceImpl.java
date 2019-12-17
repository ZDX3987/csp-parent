package com.zdx.csp.article.service;

import com.alibaba.fastjson.JSONObject;
import com.zdx.csp.api.service.ReplyService;
import com.zdx.csp.article.feign.UserServiceFeign;
import com.zdx.csp.article.mapper.ReplyMapper;
import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.common.service.ResponseService;
import com.zdx.csp.entity.Reply;
import com.zdx.csp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class ReplyServiceImpl extends ResponseService implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Override
    public List<Map> showList(Integer commentId) {
        List<Reply> list = replyMapper.showList(commentId);
        Iterator<Reply> iterator = list.iterator();
        List<Map> res = new ArrayList<>();
        while (iterator.hasNext()) {
            Reply reply = iterator.next();
            Map<String, Integer> jsonMap = new HashMap<>();
            //返回map
            Map<String, Object> map = new HashMap<>();
            jsonMap.put("id", reply.getFrom());
            String json = JSONObject.toJSONString(jsonMap);
            ResponseResult result = userServiceFeign.findById(json);
            String string = JSONObject.toJSONString(result.getData());
            map.put("from", JSONObject.parseObject(string, User.class));
            jsonMap.put("id", reply.getTo());
            json = JSONObject.toJSONString(jsonMap);
            result = userServiceFeign.findById(json);
            string = JSONObject.toJSONString(result.getData());
            map.put("to", JSONObject.parseObject(string, User.class));
            map.put("reply", reply);
            res.add(map);
        }
        return res;
    }

    @Override
    public ResponseResult showList(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer commentId = jsonObject.getInteger("commentId");
        if (commentId == null) {
            return setResultError("查询失败！");
        }
        List<Map> res = showList(commentId);
        if (res.size() <= 0) {
            return setResultSuccess("查询结果为空！");
        }
        return setResultSuccess(res);
    }

    /**
     * 递归查找所有对应一级回复的子回复
     * @param reply 一级回复
     * @param iterator 包含所有回复的迭代器
     * @return 返回子回复集合
     */
    private void getSecondReply(List<Reply> list, Reply reply, Iterator<Reply> iterator) {
        while (iterator.hasNext()) {
            Reply reply1 = iterator.next();
            //查找该一级回复的所有二级回复
            if (reply1.getReplyType() == 1) {
                if (reply1.getReply().equals(reply.getId()) || reply1.getComment().equals(reply.getComment())) {
                    list.add(reply1);
                    System.out.println(reply1);
                    getSecondReply(list, reply1, iterator);
                }
            }

        }
    }

    @Override
    public ResponseResult insert(String data) {
        if (StringUtils.isEmpty(data)) {
            return setResultError("回复失败！");
        }
        Reply reply = JSONObject.parseObject(data, Reply.class);
        reply.setCreateTime(new Date());
        System.out.println(reply);
        Integer id = replyMapper.insert(reply);
        if (id == null) {
            return setResultError("回复失败！");
        }
        System.out.println(id);
        return setResultSuccess(id);
    }

    @Override
    public ResponseResult parise(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer id = jsonObject.getInteger("id");
        Boolean isParised = jsonObject.getBoolean("isParised");
        if (id == null || isParised == null) {
            return setResultError("点赞失败！");
        }
        replyMapper.parise(id, isParised);
        return setResultSuccess("点赞成功！");
    }
}
