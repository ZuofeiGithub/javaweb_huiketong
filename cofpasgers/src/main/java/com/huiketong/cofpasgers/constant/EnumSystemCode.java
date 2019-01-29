package com.huiketong.cofpasgers.constant;

public enum EnumSystemCode {
    SUCC(2000, "SUCC"),

    ERROR_USER_NOT_EXIST(4001008, "用户不存在"),

    ERROR_TOKEN_TIMEOUT(4001010, "token失效"),

    ERROR_TOKEN(4001011, "token不正确"),

    COMMON(0, "错误");

    private final int code;
    private final String conent;

    public int getCode() {
        return code;
    }

    public String getConent() {
        return conent;
    }

    EnumSystemCode(int code, String content) {
        this.code = code;
        this.conent = content;
    }

    public static EnumSystemCode findByCode(int code) {
        for (EnumSystemCode sCode : EnumSystemCode.values()) {
            if (sCode.getCode() == code) {
                return sCode;
            }
        }
        return null;
    }
}
