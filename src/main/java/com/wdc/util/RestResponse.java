package com.wdc.util;

public class RestResponse<T> {
    /** 业务处理成功 */
    public static final String OK = "200";

    /** 业务处理失败 */
    public static final String FAIL= "201";

    private T data; // 服务端数据
    private String code = OK; // 状态码
    private String msg = ""; // 描述信息

    public static RestResponse ok() {
        return new RestResponse().setCode(OK).setMsg("操作成功");
    }

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse().setCode(OK).setData(data).setMsg("操作成功");
    }

    public static RestResponse fail() {
        return new RestResponse().setCode(FAIL).setMsg("操作异常");
    }

    public static <T> RestResponse<T> fail(String msg) {
        return new RestResponse().setCode(FAIL).setMsg(msg);
    }


    private RestResponse() {

    }

    public T getData() {
        return data;
    }

    public RestResponse setData(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RestResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RestResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
