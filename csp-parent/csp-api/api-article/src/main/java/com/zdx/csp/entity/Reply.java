package com.zdx.csp.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 回复实体类
 * @author ZDX
 */
@Getter
@Setter
public class Reply {
    private Integer id;
    private Integer comment;
    private Integer reply;
    /**
     * 0表示评论， 1表示回复
     */
    private Short replyType;
    private String text;
    private Integer from;
    private Integer to;
    private Date createTime;


    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", comment=" + comment +
                ", reply=" + reply +
                ", replyType=" + replyType +
                ", text='" + text + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", createTime=" + createTime +
                '}';
    }
}
