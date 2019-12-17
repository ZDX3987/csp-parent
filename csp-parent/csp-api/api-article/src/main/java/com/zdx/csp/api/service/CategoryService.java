package com.zdx.csp.api.service;

import com.zdx.csp.common.entity.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.core.MediaType;

/**
 * 文章分类
 */
@RequestMapping(value = "/category", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
public interface CategoryService {

    /**
     * 查找全部分类
     * @return
     */
    @RequestMapping("/list")
    ResponseResult showList();

    @RequestMapping("/listWithCount")
    ResponseResult showListWithCount();

}
