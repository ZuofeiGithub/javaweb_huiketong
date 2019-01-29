package com.huiketong.cofpasgers.weixinpay.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

//gson工具类
public class GsonUtils {
	//普通转换
	//private static Gson gson=new Gson();
	//使用GsonBuilder可以设定转换时的日期格式
	private static Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
	//将object转换成json格式数据
	public static String object2json(Object o){
		return gson.toJson(o);
	}
	//将json格式数据转换成Object
	@SuppressWarnings("unchecked")
	public static Object json2object(String s,@SuppressWarnings("rawtypes") Class c){
		return gson.fromJson(s, c);
	}
	//将json格式数据转换成List
	@SuppressWarnings("rawtypes")
	public static List json2list(String s,TypeToken tt){
		return gson.fromJson(s, tt.getType()); 
	}
}
