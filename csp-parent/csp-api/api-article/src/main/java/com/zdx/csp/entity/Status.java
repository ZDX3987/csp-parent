package com.zdx.csp.entity;

/**
 * 文章状态实体类
 */
public class Status {
    private Integer id;
    private String alias;

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

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                '}';
    }
}
