package com.zdx.csp.article.service;

import com.alibaba.fastjson.JSONObject;
import com.zdx.csp.api.service.CommentService;
import com.zdx.csp.api.service.ReplyService;
import com.zdx.csp.article.feign.UserServiceFeign;
import com.zdx.csp.article.mapper.CommentMapper;
import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.common.service.ResponseService;
import com.zdx.csp.entity.Comment;
import com.zdx.csp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class CommentServiceImpl extends ResponseService implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private ReplyService replyService;


    @Override
    public ResponseResult parise(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer id = jsonObject.getInteger("id");
        Boolean isParised = jsonObject.getBoolean("isParised");
        if (id == null || isParised == null) {
            return setResultError("点赞失败！");
        }
        commentMapper.parise(id, isParised);
        return setResultSuccess("点赞成功！");
    }

    @Override
    public ResponseResult insert(String data) {
        if (StringUtils.isEmpty(data)) {
            return setResultError("评论失败！");
        }
        JSONObject jsonObject = JSONObject.parseObject(data);
        System.out.println(jsonObject);
        Comment comment = new Comment();
        comment.setText(jsonObject.getString("text"));
        comment.setArticle(jsonObject.getInteger("articleId"));
        comment.setUser(jsonObject.getInteger("userId"));
        comment.setCreateTime(new Date());
        Integer id = commentMapper.insert(comment);
        if (id == null) {
            return setResultError("评论失败！");
        }
        log.info("评论成功！");
        return setResultSuccess(id);
    }

    @Override
    public ResponseResult getByComment(String data) {
        System.out.println(data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer articleId = jsonObject.getInteger("articleId");
        if (articleId == null) {
            return setResultError("查询失败！");
        }
        //查询评论列表信息
        List<Comment> list = commentMapper.getByArticle(articleId);
        if (list.size() <= 0) {
            return setResultSuccess("查询记录为空！");
        }
        Iterator<Comment> iterator = list.iterator();
        //返回结果集合
        List<Map> res = new ArrayList<>();
        while (iterator.hasNext()) {
            Comment comment = iterator.next();
            Map<String, Integer> jsonMap = new HashMap<>();
            jsonMap.put("id", comment.getUser());
            String json = JSONObject.toJSONString(jsonMap);
            //获取User信息
            ResponseResult result = userServiceFeign.findById(json);
            String string = JSONObject.toJSONString(result.getData());
            //封装结果集
            Map<String, Object> map = new HashMap<>();
            map.put("comment", comment);
            map.put("user", JSONObject.parseObject(string, User.class));
            //封装回复
            List<Map> replys = replyService.showList(comment.getId());
            map.put("replys", replys);
            res.add(map);
        }
        return setResultSuccess(res);
    }
}
