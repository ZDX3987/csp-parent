package com.zdx.csp.article.service;

import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zdx.csp.api.service.ArticleService;
import com.zdx.csp.article.feign.UserServiceFeign;
import com.zdx.csp.article.mapper.ArticleMapper;
import com.zdx.csp.article.mapper.CategoryMapper;
import com.zdx.csp.common.constants.ResponseCode;
import com.zdx.csp.common.entity.EditorResponse;
import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.common.service.ResponseService;
import com.zdx.csp.entity.Article;
import com.zdx.csp.entity.Category;
import com.zdx.csp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class ArticleServiceImpl extends ResponseService implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private FastFileStorageClient fileStorageClient;
    @Autowired
    private UserServiceFeign userServiceFeign;

    @Value("${fastdfs.host}")
    private String host;


    @Override
    public ResponseResult collected(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer userId = jsonObject.getInteger("userId");
        Integer articleId = jsonObject.getInteger("articleId");
        if (userId == null || articleId == null) {
            return setResultError("查询失败！");
        }
        Integer collected = articleMapper.collected(articleId, userId);
        return setResultSuccess(collected);
    }

    @Override
    public ResponseResult getByCate(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer cateId = jsonObject.getInteger("cateId");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageCount = jsonObject.getInteger("pageCount");
        if (cateId == null || pageNum == null || pageCount == null) {
            return setResultError("查询失败！");
        }
        int currentIndex = (pageNum-1)*pageCount;
        List<Article> list = articleMapper.getByCate(cateId, currentIndex, pageCount);
        //返回结果集合
        List<Map> res = new ArrayList<>();
        Iterator<Article> iterator = list.iterator();

        //查询分类信息
        Category category = categoryMapper.getById(cateId);

        while (iterator.hasNext()) {
            Article article = iterator.next();
            //查询作者信息
            Map<String, Integer> idMap = new HashMap<>();
            idMap.put("id", article.getAuthor());
            String json = JSONObject.toJSONString(idMap);
            ResponseResult result = userServiceFeign.findById(json);
            String string = JSONObject.toJSONString(result.getData());
            User user = JSONObject.parseObject(string, User.class);

            //封装结果集
            Map<String, Object> map = new HashMap<>();
            map.put("article", article);
            map.put("user", user);
            map.put("category", category);
            res.add(map);
        }
        return setResultSuccess(res);
    }

    @Override
    public ResponseResult getByUser(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer uId = jsonObject.getInteger("uId");
        if (uId == null) {
            setResultError("查询失败！");
        }
        List<Article> articles = articleMapper.getByUser(uId);
        return setResultSuccess(articles);
    }

    @Override
    public ResponseResult getUserById(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer userId = jsonObject.getInteger("userId");
        if (userId == null) {
            return setResultError("查询失败！");
        }
        List<Article> list = articleMapper.getByUserId(userId);
        return setResultSuccess(list);
    }

    @Override
    public ResponseResult collectArticle(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Boolean hasCollected = jsonObject.getBoolean("hasCollected");
        Integer aId = jsonObject.getInteger("aId");
        Integer uId = jsonObject.getInteger("uId");
        if (hasCollected == null || aId == null || uId == null) {
            return setResultError("收藏失败！");
        }
        if (hasCollected) {
            articleMapper.unCollect(aId, uId);
            return setResultSuccess("取消收藏成功！");
        }
        else {
            articleMapper.collectArticle(aId, uId);
            return setResultSuccess("收藏成功！");
        }
    }

    @Override
    public ResponseResult getById(Integer id) {
        if (id == null) {
            return setResultError("查询失败！");
        }
        Article article = articleMapper.getById(id);
        Category category = categoryMapper.getById(article.getCategory());
        Map<String, Integer> idMap = new HashMap<>();
        idMap.put("id", article.getAuthor());
        String json = JSONObject.toJSONString(idMap);
        ResponseResult result = userServiceFeign.findById(json);

        String string = JSONObject.toJSONString(result.getData());
        User user = JSONObject.parseObject(string, User.class);
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        map.put("user", user);
        map.put("category", category);
        return setResultSuccess(map);
    }

    @Override
    public ResponseResult createArticle(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Article article = JSONObject.parseObject(jsonObject.getString("article"), Article.class);
        if (article == null) {
            return setResultError("发布失败！");
        }

        //封装完整的文章
        ResponseResult result = userServiceFeign.findByToken(data);
        if (!result.getStatus().equals(ResponseCode.HTTP_RES_CODE_200.getCode())) {
            return setResultError("发布失败！");
        }
        String string = JSONObject.toJSONString(result.getData());
        User user = JSONObject.parseObject(string, User.class);
        article.setAuthor(user.getId());
        article.setCreateTime(new Date());
        Integer id = articleMapper.create(article);
        if (id == null) {
            return setResultError("发布失败！");
        }
        return setResultSuccess("发布成功！");
    }

    @Override
    public ResponseResult showList(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        String order = jsonObject.getString("order");
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageCount = jsonObject.getInteger("pageCount");
        if (order == null || pageNum == null || pageCount == null) {
            return setResultError("查询失败！");
        }
        int currentIndex = (pageNum-1)*pageCount;
        List<Article> list = articleMapper.showList(order, currentIndex, pageCount);
        //返回结果集合
        List<Map> res = new ArrayList<>();
        Iterator<Article> iterator = list.iterator();
        while (iterator.hasNext()) {
            Article article = iterator.next();
            //查询作者信息
            Map<String, Integer> idMap = new HashMap<>();
            idMap.put("id", article.getAuthor());
            String json = JSONObject.toJSONString(idMap);
            ResponseResult result = userServiceFeign.findById(json);
            String string = JSONObject.toJSONString(result.getData());
            User user = JSONObject.parseObject(string, User.class);
            //查询分类信息
            Category category = categoryMapper.getById(article.getCategory());
            //封装结果集
            Map<String, Object> map = new HashMap<>();
            map.put("article", article);
            map.put("user", user);
            map.put("category", category);
            res.add(map);
        }
        return setResultSuccess(res);
    }

    @Override
    public ResponseResult parise(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer id = jsonObject.getInteger("id");
        Boolean isParised = jsonObject.getBoolean("isParised");
        if (id == null || isParised == null) {
            return setResultError("点赞失败！");
        }
        articleMapper.parise(id, isParised);
        return setResultSuccess("点赞成功！");
    }

    @Override
    public ResponseResult collectionArticle(Integer id) {
        return null;
    }

    @Override
    public ResponseResult uploadThumbnail(MultipartFile file) {
        if (file == null) {
            return setResultError("图片不能为空！");
        }
        try {
            String imgUrl = upload(file);
            return setResultSuccess("图片上传成功！", imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return setResultError("图片上传失败！");
        }

    }

    @Override
    public EditorResponse uploadImg(MultipartFile file) {
        if (file == null) {
            return new EditorResponse(1, null);
        }
        try {
            List<String> list = new ArrayList<>();
            String imgUrl = upload(file);
            list.add(imgUrl);
            return new EditorResponse(0, list);
        } catch (IOException e) {
            e.printStackTrace();
            return new EditorResponse(1, null);
        }
    }

    @Override
    public ResponseResult delete(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer id = jsonObject.getInteger("id");
        if (id == null) {
            return setResultError("删除失败！");
        }
        articleMapper.delete(id);
        return setResultSuccess("删除成功！");
    }

    private String upload(MultipartFile file) throws IOException {
        StorePath path = fileStorageClient.uploadFile(file.getInputStream(), file.getSize(),
                FilenameUtils.getExtension(file.getOriginalFilename()), null);
        String imgUrl = "http://" + host +"/"+ path.getFullPath();
        log.info("imgUrl:{}", imgUrl);
        log.info("OriginalFilename:{}", file.getOriginalFilename());
        return imgUrl;
    }
}
