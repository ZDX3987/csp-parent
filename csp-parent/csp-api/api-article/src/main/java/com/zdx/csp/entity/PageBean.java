package com.zdx.csp.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页实体类
 */
@Getter
@Setter
public class PageBean {

    private int pageNum;
    private int pageCount;
    private long totalCount;
    private int totalPage;

}
