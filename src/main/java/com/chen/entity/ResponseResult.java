package com.chen.entity;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private Object data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果 不带对象
     *
     * @param msg
     * @return
     */
    public static ResponseResult success(String msg) {
        return new ResponseResult(200, msg, null);
    }


    /**
     * 成功返回结果 带对象
     *
     * @param msg
     * @param obj
     * @return
     */
    public static ResponseResult success(String msg, Object data) {
        return new ResponseResult(200, msg, data);
    }

    /**
     * 失败返回结果
     *
     * @param msg
     * @return
     */
    public static ResponseResult error(String msg) {
        return new ResponseResult(500, msg, null);
    }

    /**
     * 失败返回结果
     *
     * @param msg
     * @param data
     * @return
     */
    public static ResponseResult error(String msg, Object data) {
        return new ResponseResult(500, msg, data);
    }
}