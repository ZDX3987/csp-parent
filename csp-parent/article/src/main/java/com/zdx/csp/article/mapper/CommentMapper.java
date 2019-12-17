package com.zdx.csp.article.mapper;

import com.zdx.csp.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 新建评论
     * @param comment
     * @return
     */
    Integer insert(Comment comment);

    /**
     * 根据文章查询所有评论
     * @param articleId
     * @return
     */
    List<Comment> getByArticle(Integer articleId);

    /**
     * 点赞
     * @return
     */
    Integer parise(@Param("id") Integer id, @Param("isParised") Boolean isParised);
}
