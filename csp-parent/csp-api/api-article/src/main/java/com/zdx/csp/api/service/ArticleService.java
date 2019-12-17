package com.zdx.csp.api.service;

import com.zdx.csp.common.entity.EditorResponse;
import com.zdx.csp.common.entity.ResponseResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;

@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
public interface ArticleService {

    @RequestMapping("/collected")
    ResponseResult collected(@RequestBody String data);

    @RequestMapping("/getByCate")
    ResponseResult getByCate(@RequestBody String data);

    @RequestMapping("/getCollect")
    ResponseResult getByUser(@RequestBody String data);

    @RequestMapping("/ownArticle")
    ResponseResult getUserById(@RequestBody String data);

    /**
     * 收藏文章
     * @param data
     * @return
     */
    @RequestMapping("/collect")
    ResponseResult collectArticle(@RequestBody String data);

    /**
     * 通过Id获取
     * @param id
     * @return
     */
    @RequestMapping("/{id}")
    ResponseResult getById(@PathVariable("id") Integer id);
    /**
     * 发表文章
     * @param
     * @return
     */
    @RequestMapping("/create")
    ResponseResult createArticle(@RequestBody String data);

    /**
     * 分页显示
     * @return
     */
    @RequestMapping("/articleList")
    ResponseResult showList(@RequestBody String data);

    /**
     * 点赞
     * @return
     */
    @RequestMapping("/parise")
    ResponseResult parise(@RequestBody String data);

    /**
     * 收藏文章
     * @param id
     * @return
     */
    @RequestMapping("/collection")
    ResponseResult collectionArticle(@RequestParam("id") Integer id);

    /**
     * 上传文章缩略图
     * @param file
     * @return 返回图片URL
     */
    @RequestMapping("/thumbnail")
    ResponseResult uploadThumbnail(@RequestParam("file") MultipartFile file);

    /**
     * 上传文章内图片
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    EditorResponse uploadImg(@RequestParam("file") MultipartFile file);

    @RequestMapping("/delete")
    ResponseResult delete(@RequestBody String data);

}
