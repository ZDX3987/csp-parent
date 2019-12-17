package com.zdx.csp.api.service;

import com.zdx.csp.common.entity.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 回复服务
 */
@RequestMapping(value = "/reply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
public interface ReplyService {

    List<Map> showList(Integer commentId);

    @RequestMapping("show")
    ResponseResult showList(@RequestBody String data);

    @RequestMapping("/add")
    ResponseResult insert(@RequestBody String data);

    /**
     * 点赞
     * @param data
     * @return
     */
    @RequestMapping("/parise")
    ResponseResult parise(@RequestBody String data);
}
