package com.zdx.csp.api.service;


import com.zdx.csp.common.entity.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.core.MediaType;

@RequestMapping(value = "/comment", method = RequestMethod.POST,  produces = MediaType.APPLICATION_JSON)
public interface CommentService {

    /**
     * 点赞
     * @param data
     * @return
     */
    @RequestMapping("/parise")
    ResponseResult parise(@RequestBody String data);

    /**
     *添加评论
     * @param data
     * @return
     */
    @RequestMapping("/add")
    ResponseResult insert(@RequestBody String data);

    @RequestMapping("/show")
    ResponseResult getByComment(@RequestBody String data);
}
