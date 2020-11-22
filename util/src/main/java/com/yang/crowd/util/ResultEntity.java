package com.yang.crowd.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultEntity<T> {
    public static final String SUCCESS="SUCCESS";
    public static final String FAILED="FAILED";
    /**
     * 处理结果
     * 返回信息
     * 返回数据
     */
    private String result;
    private String message;
    private T data;

    public ResultEntity(String result,String message,T data){
        this.result=result;
        this.message=message;
        this.data=data;
    }
    public static <Type> ResultEntity<Type> successWithoutData(){
        return new ResultEntity<>(SUCCESS,null,null);
    }
    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<>(SUCCESS,null,data);
    }
    public static <Type> ResultEntity<Type> failed(String message){
        return new ResultEntity<>(FAILED,message,null);
    }
}
