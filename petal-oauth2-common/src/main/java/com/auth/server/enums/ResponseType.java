package com.auth.server.enums;


/**
 * 响应类型枚举类
 *
 * @author youzhengjie
 * @date 2023-04-19 12:17:30
 */
public enum ResponseType {

    /**
     * 通用状态
     */
    SUCCESS(200,"接口请求成功"),
    ERROR(500,"接口请求失败"),

    UNAUTHORIZED(401,"认证失败"),
    FORBIDDEN(403,"拒绝访问")

    ;


    private int code;
    private String message;

    ResponseType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
