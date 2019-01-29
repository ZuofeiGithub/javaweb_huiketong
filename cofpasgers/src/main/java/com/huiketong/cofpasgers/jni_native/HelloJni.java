package com.huiketong.cofpasgers.jni_native;

/**
 * @Author: 左飞
 * @Date: 2019/1/28 9:03
 * @Version 1.0
 */
public class HelloJni {
    static{
        System.loadLibrary("Hello");
    }
    public static native void displayHelloWorld();
}
