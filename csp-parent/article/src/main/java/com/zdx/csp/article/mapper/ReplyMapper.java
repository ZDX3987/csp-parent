package com.zdx.csp.article.mapper;

import com.zdx.csp.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回复
 */
@Mapper
public interface ReplyMapper {

    List<Reply> showList(Integer commentId);

    Integer insert(Reply reply);

    /**
     * 点赞
     * @return
     */
    Integer parise(@Param("id") Integer id, @Param("isParised") Boolean isParised);
}
