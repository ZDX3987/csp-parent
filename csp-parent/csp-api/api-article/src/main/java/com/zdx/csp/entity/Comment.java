package com.zdx.csp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 评论实体类
 * @author ZDX
 */
@Getter
@Setter
public class Comment {
    private Integer id;
    private String text;
    private Integer article;
    private Integer user;
    private Date createTime;
    private Integer parise;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", article=" + article +
                ", user=" + user +
                ", createTime=" + createTime +
                ", parise=" + parise +
                '}';
    }
}
