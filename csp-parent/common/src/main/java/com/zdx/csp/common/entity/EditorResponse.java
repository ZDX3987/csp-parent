package com.zdx.csp.common.entity;

/**
 * 前端文本编辑器上传图片的响应
 */
public class EditorResponse {

    private Integer errno;
    private Object data;

    public EditorResponse() {}

    public EditorResponse(Integer errno, Object data) {
        this.errno = errno;
        this.data = data;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
