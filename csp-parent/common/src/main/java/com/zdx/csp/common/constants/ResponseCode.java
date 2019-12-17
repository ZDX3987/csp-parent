package com.zdx.csp.common.constants;

public enum ResponseCode {

    /**
     * 响应成功状态码
     */
    HTTP_RES_CODE_200(200, "success"),

    /**
     * 响应失败状态码
     */
    HTTP_RES_CODE_500(500, "error");

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
