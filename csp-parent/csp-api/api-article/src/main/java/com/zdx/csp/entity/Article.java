package com.zdx.csp.entity;

import java.util.Date;

/**
 * 文章实体类
 */
public class Article {
    private Integer id;
    private String title;
    private String content;
    private String digest;
    private Date createTime;
    private Integer author;
    private int parise;
    private String imgUrl;
    private Integer category;
    private Status status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public int getParise() {
        return parise;
    }

    public void setParise(int parise) {
        this.parise = parise;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", digest='" + digest + '\'' +
                ", createTime=" + createTime +
                ", author=" + author +
                ", parise=" + parise +
                ", imgUrl='" + imgUrl + '\'' +
                ", category=" + category +
                ", status=" + status +
                '}';
    }
}
