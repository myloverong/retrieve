package com.xiaour.spring.boot.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(200),//成功
    FAIL(400),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    NOT_FOUND(404),//接口不存在
    NO_ACCESS(501),//网络繁忙
    NO_YANZHENG(102),
    NO_RETURN(103),
    NO_QUANXIAN(108),
    NO_FPZL(109),
    INTERNAL_SERVER_ERROR(500);//服务器内部错误

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}

