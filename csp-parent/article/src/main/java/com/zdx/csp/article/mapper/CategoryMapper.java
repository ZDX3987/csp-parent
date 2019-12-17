package com.zdx.csp.article.mapper;

import com.zdx.csp.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 根据ID获取category
     * @param id
     * @return
     */
    Category getById(Integer id);

    /**
     * 获取category列表
     * @return
     */
    List<Category> showList();
}
