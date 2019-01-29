package com.huiketong.cofpasgers.constant;

import com.huiketong.cofpasgers.entity.Enterprise;
import java.util.List;

public class JSONData {
    Integer code;
    String msg;
    Integer count;
    Object data;

    public Integer getCode() {
        return code;
    }

    public JSONData setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public JSONData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public JSONData setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JSONData setData(Object data) {
        this.data = data;
        return this;
    }
}
