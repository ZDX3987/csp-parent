package com.zdx.csp.entity;

/**
 * 文章分类实体类
 */
public class Category {
    private Integer id;
    private String alias;
    private Category parent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", parent=" + parent +
                '}';
    }
}
