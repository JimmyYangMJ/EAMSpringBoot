package com.mc.eam.repairs.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * status 状态码 （0：正常）
 * msg 提示信息 （查询成功）
 * data<T> 数据
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
// 比如 msg 为null 时不会传送json = null 给前端
public class ServerResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "ServerResponse{" +
                "code =" + code +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    private ServerResponse(int code){
        this.code = code;
    }

    private ServerResponse(int code, T data){
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @JsonIgnore
    // 使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }

    // 下面的三个 public方法会显示在json中
    public int getCode(){
        return code;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

    public static <T> ServerResponse<T> createByNull(){
        return new ServerResponse<T>(ResponseCode.NULL.getCode(),ResponseCode.NULL.getDesc());
    }

    public static <T> ServerResponse<T> createByNullMessage(String nullMessage){
        return new ServerResponse<T>(ResponseCode.NULL.getCode(), nullMessage);
    }

    public static <T> ServerResponse<T> createByNullCodeMessage(int nullCode,String nullMessage){
        return new ServerResponse<T>(nullCode, nullMessage);
    }













}
