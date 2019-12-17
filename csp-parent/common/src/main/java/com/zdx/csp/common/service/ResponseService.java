package com.zdx.csp.common.service;


import com.zdx.csp.common.constants.ResponseCode;
import com.zdx.csp.common.entity.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseService {

    @Autowired
    protected RedisService redisService;

    /**
     * 返回错误，自定义错误信息
     */
    public ResponseResult setResultError(String msg) {
        return setResult(ResponseCode.HTTP_RES_CODE_500.getCode(), msg, null);
    }

    /**
     * 返回成功，以及data数据
     */
    public ResponseResult setResultSuccess(Object data) {
        return setResult(ResponseCode.HTTP_RES_CODE_200.getCode(),
                ResponseCode.HTTP_RES_CODE_200.getMsg(), data);
    }

    /**
     * 返回成功，以及msg，data数据
     */
    public ResponseResult setResultSuccess(String msg, Object data) {
        return setResult(ResponseCode.HTTP_RES_CODE_200.getCode(), msg, data);
    }

    /**
     * 返回成功，无数据
     */
    public ResponseResult setResultSuccess() {
        return setResult(ResponseCode.HTTP_RES_CODE_200.getCode(),
                ResponseCode.HTTP_RES_CODE_200.getMsg(), null);
    }

    /**
     * 返回成功，自定义msg
     */
    public ResponseResult setResultSuccess(String msg) {
        return setResult(ResponseCode.HTTP_RES_CODE_200.getCode(), msg, null);
    }

    /**
     * 封装响应数据
     */
    public ResponseResult setResult(Integer status, String msg, Object data){
        return new ResponseResult(status, msg, data);
    }
}
