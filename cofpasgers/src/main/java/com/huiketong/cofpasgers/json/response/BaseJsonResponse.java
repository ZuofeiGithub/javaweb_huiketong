package com.huiketong.cofpasgers.json.response;

import lombok.Data;

@Data
public class BaseJsonResponse {
    String code;
    String msg;
    Object data;

    public String getCode() {
        return code;
    }

    public BaseJsonResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseJsonResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BaseJsonResponse setData(Object data) {
        this.data = data;
        return this;
    }
}
