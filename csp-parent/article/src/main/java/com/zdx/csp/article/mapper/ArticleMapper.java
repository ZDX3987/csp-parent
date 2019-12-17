package com.zdx.csp.article.mapper;

import com.zdx.csp.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {

    /**
     *
     * @param articleId
     * @param userId
     * @return
     */
    Integer collected(@Param("articleId") Integer articleId, @Param("userId") Integer userId);

    /**
     * 分类查找
     * @param uId
     * @return
     */
    List<Article> getByCate(@Param("cateId") Integer uId, @Param("currentIndex") Integer currentIndex,
                            @Param("pageCount") Integer pageCount);

    /**
     * 通过ID获取自己的文章
     * @param userId
     * @return
     */
    List<Article> getByUserId(Integer userId);

    /**
     * 通过用户获取收藏的文章
     * @param uId
     * @return
     */
    List<Article> getByUser(Integer uId);
    /**
     * 收藏文章
     * @param aId 文章ID
     * @param uId 用户ID
     * @return
     */
    Integer collectArticle(@Param("aId") Integer aId, @Param("uId") Integer uId);


    Integer unCollect(@Param("aId") Integer aId, @Param("uId") Integer uId);

    /**
     * 通过ID获取
     * @param id
     * @return
     */
    Article getById(Integer id);
    /**
     * 新建文章
     * @param article
     * @return
     */
    Integer create(Article article);

    /**
     * 点赞
     * @param id
     * @return
     */
    Integer parise(@Param("id") Integer id, @Param("isParised") Boolean isParised);

    /**
     * 分页查询文章
     * @return
     */
    List<Article> showList(@Param("order") String order, @Param("currentIndex") int currentIndex, @Param("pageCount") int pageCount);

    /**
     *按分类查找文章数量
     * @return
     */
    List<Map<String, Integer>> getCountByCate();

    /**
     * 删除文章
     * @param id
     * @return
     */
    Integer delete(Integer id);
}
