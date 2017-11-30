package com.buswe.module.core.pojo;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAIL(500,"操作失败")
    ;
    public int code;
    public String message;

    private ResultCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}
