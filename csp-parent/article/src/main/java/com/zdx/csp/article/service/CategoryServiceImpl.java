package com.zdx.csp.article.service;

import com.zdx.csp.api.service.CategoryService;
import com.zdx.csp.article.mapper.ArticleMapper;
import com.zdx.csp.article.mapper.CategoryMapper;
import com.zdx.csp.common.entity.ResponseResult;
import com.zdx.csp.common.service.ResponseService;
import com.zdx.csp.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class CategoryServiceImpl extends ResponseService implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public ResponseResult showList() {
        List<Category> list = categoryMapper.showList();
        return setResultSuccess(list);
    }

    @Override
    public ResponseResult showListWithCount() {
        //获取所有分类信息
        List<Category> cateList = categoryMapper.showList();
        //获取所有分类信息及文章个数集合
        List<Map<String, Integer>> countList = articleMapper.getCountByCate();
        Iterator<Map<String, Integer>> countIterator = countList.iterator();
        Iterator<Category> cateIterator = cateList.iterator();

        //返回信息集合
        List<Map<String, Object>> list = new ArrayList<>();

        for (Category cate:cateList) {
            for (Map<String, Integer> map:countList) {
                if (cate.getId().equals(map.get("category"))) {
                    Map<String, Object> resMap = new HashMap<>();
                    resMap.put("category", cate);
                    resMap.put("count", map.get("count"));
                    list.add(resMap);
                }
            }
        }
        return setResultSuccess(list);
    }
}
